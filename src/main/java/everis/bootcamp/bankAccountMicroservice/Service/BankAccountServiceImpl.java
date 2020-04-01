package everis.bootcamp.bankAccountMicroservice.Service;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccount;
import everis.bootcamp.bankAccountMicroservice.Document.BankAccountTransaction;
import everis.bootcamp.bankAccountMicroservice.Document.BankAccountType;
import everis.bootcamp.bankAccountMicroservice.Repository.BankAccountRepository;
import everis.bootcamp.bankAccountMicroservice.Repository.BankAccountTransactionRepository;
import everis.bootcamp.bankAccountMicroservice.Repository.BankAccountTypeRepository;
import everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request.AddBankAccountRequest;
import everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request.CreditPaymentRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    BankAccountRepository bankAccountRepository;
    @Autowired
    BankAccountTransactionRepository bankAccountTransactionRepository;
    @Autowired
    BankAccountTypeRepository bankAccountTypeRepository;

    /*VALIDATION AND CONDITIONS SECTION*/
    private Boolean hasBankAccount(String id) {

        Boolean clientAccount = bankAccountRepository.existsByClientId(id).block();

        if (clientAccount == true) {
            System.out.println("TIENE CUENTA BANCARIA");
            return true;
        } else {
            System.out.println("NO POSEE CUENTA BANCARIA");
            return false;
        }
    }

    private Mono<Long> countAccounts(String clientId) {
        return bankAccountRepository.findAllByClientId(clientId).count();
    }

    private Boolean existInClient(String id) {
        String url = "http://localhost:8001/api/clients/exist/" + id;
        Mono<Boolean> client = WebClient.create()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Boolean.class);

        Boolean value = client.block();
        if (value == true) {
            System.out.println("ES CLIENTE");
        } else {
            System.out.println("NO ES CLIENTE");
        }
        return value;

    }

    private Mono<String> clientType(String id) {
        String url = "http://localhost:8001/api/clients/type/" + id;
        Mono<String> reciver = WebClient.create()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class);
        Mono<String> type = reciver.map(s -> {
            return s.toString();
        });
        return type;
    }

    //CRUD AND IMPLEMENTATIONS

    @Override
    public Mono<BankAccount> create(AddBankAccountRequest addBankAccountRequest) {
        Mono<BankAccountType> bankAccountTypeMono =
                bankAccountTypeRepository.findById(addBankAccountRequest.getIdBankAccountType())
                        .switchIfEmpty(Mono.error(new Exception("TIPO DE CUENTA BANCARIA ERRONEO")));

        //Testing NO BORRAR
        Mono<String> typoacc = bankAccountTypeMono
                .map(bankAccountType -> {
                    return bankAccountType.getName();
                });
        Mono<String> typoacc2 = bankAccountTypeMono
                .map(bankAccountType -> {
                    return bankAccountType.getId();
                });
        Mono<Double> typoacc3 = bankAccountTypeMono
                .map(bankAccountType -> {
                    return bankAccountType.getMinCreationAmmount();
                });
        System.out.println("TIPO   ID:    ->" + typoacc2.block());
        System.out.println("TIPO NAME:    ->" + typoacc.block());
        System.out.println("TIPO  MIN:    ->" + typoacc3.block());

        List<String> clasesUnicas = Arrays.asList("cuentaAhorroPersonalVIP", "cuentaCorrientePersonalVIP",
                "empresarialPYME", "empresarialCorporativo");


        /*VERSION OMEGA: No implementar el flatMap debido a bucle infinito que devuelve Error 503*/
        //return bankAccountTypeMono.flatMap(accountType -> {
        BankAccount bankAccount = new BankAccount();
        BeanUtils.copyProperties(addBankAccountRequest, bankAccount);
        bankAccount.setClientId(addBankAccountRequest.getClientId());
        bankAccount.setSerialNumber(addBankAccountRequest.getSerialNumber());
        bankAccount.setDni(addBankAccountRequest.getDni());
        bankAccount.setMonto(addBankAccountRequest.getMonto());
        bankAccount.setComision(addBankAccountRequest.getComision());
        Set<String> holders = new HashSet<>();
        holders.add(bankAccount.getDni());
        bankAccount.setHolders(holders);
        bankAccount.setSigners(new HashSet<>());
        bankAccount.setTransactionLeft(addBankAccountRequest.getTransactionLeft());
        bankAccount.setBankAccountType(BankAccountType.builder()
                .id(typoacc2.block())
                .name(typoacc.block())
                .build());


        String type = clientType(bankAccount.getClientId()).block().trim();

        //if (clientType(bankAccount.getClientId()).equals(Mono.just("Personal"))) {
        if (clientType(bankAccount.getClientId()).block().trim().equalsIgnoreCase("Personal")) {
            //################### VALIDACION SI TYPO PERSONAL ########################
            System.out.println("TIPO: PERSONAL");

            if (hasBankAccount(bankAccount.getClientId())) {
                System.out.println("HAS ACCOUNT");
                return Mono.empty();
            } else {
                if (existInClient(bankAccount.getClientId())) {
                    System.out.println("ONLY CLIENT");
                    BankAccountTransaction firstTransaction = new BankAccountTransaction();
                    firstTransaction.setIdCliente(addBankAccountRequest.getClientId());
                    firstTransaction.setSerialNumber(addBankAccountRequest.getSerialNumber());
                    firstTransaction.setTransferenceType("CREATING PERSONAL ACCOUNT");
                    firstTransaction.setTransferenceAmount(bankAccount.getMonto());
                    firstTransaction.setTotalAmount(bankAccount.getMonto());
                    bankAccountTransactionRepository.save(firstTransaction).subscribe();
                    return bankAccountRepository.save(bankAccount);
                } else {
                    System.out.println("NOT EVEN CLIENT");
                    return Mono.empty();

                }
            }
        } else if (type.equalsIgnoreCase("Empresarial")) {
            //################### VALIDACION SI TYPO EMPRESARIAL ########################
            System.out.println("TIPO: EMPRESARIAL");

            //if (typoacc2.block().trim().equalsIgnoreCase("Cuentacorriente")) {
            if (bankAccount.getBankAccountType().getName().trim().equalsIgnoreCase("Cuentacorriente")) {
                System.out.println("CREATING CUENTA CORRIENTE");
                BankAccountTransaction firstTransaction = new BankAccountTransaction();
                firstTransaction.setIdCliente(addBankAccountRequest.getClientId());
                firstTransaction.setSerialNumber(addBankAccountRequest.getSerialNumber());
                firstTransaction.setTransferenceType("CREATING BUSINESS ACCOUNT");
                firstTransaction.setTransferenceAmount(bankAccount.getMonto());
                firstTransaction.setTotalAmount(bankAccount.getMonto());
                bankAccountTransactionRepository.save(firstTransaction).subscribe();
                return bankAccountRepository.save(bankAccount);
            } else {
                return Mono.error(new Exception("NO PUEDE CREAR ESTE TIPO DE CUENTA"));
            }
        } else if ((type.equalsIgnoreCase("PersonaVIP")) || (type.equalsIgnoreCase("PYME")) ||
                (type.equalsIgnoreCase("Corporativo"))) {

            System.out.println("TIPO: UNICO");

            //VALIDAR DATA PARA CUENTAS ESPECIALES PARTE 1
            if (clasesUnicas.stream().anyMatch(s -> s.equals(bankAccount.getBankAccountType().getName()))) {
                System.out.println("TIPO: UNICO PARTE 1     ->      Asignacion");
                bankAccount.setMinBalance(addBankAccountRequest.getMinBalance());

                if (bankAccount.getMinBalance() <= 0) {
                    return Mono.error(new Exception("SALDO MINIMO DE FINAL DE MES NO PUEDE SER MENOR IGUAL A 0"));
                }
                if (bankAccount.getMonto() < typoacc3.block()) {
                    System.out.println(bankAccount.getMonto());
                    return Mono.error(new Exception("MONTO INGRESADO ES MENOR AL MINIMO REQUERIDO"));
                }
            }

            //VALIDAR DATA PARA CUENTAS ESPECIALES PARTE 1
            BankAccountTransaction firstTransaction = new BankAccountTransaction();
            firstTransaction.setIdCliente(addBankAccountRequest.getClientId());
            firstTransaction.setSerialNumber(addBankAccountRequest.getSerialNumber());
            firstTransaction.setTransferenceType("CREATING OTHER TYPE ACCOUNT");
            firstTransaction.setTransferenceAmount(bankAccount.getMonto());
            firstTransaction.setTotalAmount(bankAccount.getMonto());
            bankAccountTransactionRepository.save(firstTransaction).subscribe();
            return bankAccountRepository.save(bankAccount);
        } else {
            return Mono.error(new Exception("EL TIPO DE CLIENTE INGRESADO ES INCORRECTO - INGRESE CORRECTAMENTE"));
        }
        //});

    }

    @Override
    public Mono<BankAccount> update(String id, AddBankAccountRequest addBankAccountRequest) {
        return bankAccountRepository.findById(id).flatMap(client -> {
            client.setSerialNumber(addBankAccountRequest.getSerialNumber());
            //client.setType(addBankAccountRequest.getType());      ->  NO lo pongo porque no deberia poder cambiarse
            client.setDni(addBankAccountRequest.getDni());
            client.setTransactionLeft(addBankAccountRequest.getTransactionLeft());
            client.setMinBalance(addBankAccountRequest.getMinBalance());
            client.setComision(addBankAccountRequest.getComision());
            Set<String> holders = new HashSet<>();
            holders.addAll(client.getHolders());
            client.setHolders(holders);
            Set<String> signers = new HashSet<>();
            signers.addAll(client.getSigners());
            client.setHolders(signers);
            client.setClientId(addBankAccountRequest.getClientId());
            return bankAccountRepository.save(client);
        });

    }

    @Override
    public Flux<BankAccount> readAll(String clientId) {
        return bankAccountRepository.findAllByClientId(clientId);
    }

    @Override
    public Mono<BankAccount> delete(String id) {
        return getOne(id).switchIfEmpty(Mono.empty()).filter(Objects::nonNull)
                .flatMap(bankAccount -> bankAccountRepository.delete(bankAccount).then(Mono.just(bankAccount)));
    }


    @Override
    public Mono<BankAccount> getOne(String id) {
        return bankAccountRepository.findById(id);
    }

    @Override
    public Mono<Boolean> isPresent(String clientId) {
        return bankAccountRepository.existsByClientId(clientId);
    }

//    @Override
//    public Mono<BankAccount> tranference(String id, BankAccountTransaction bankAccountTransaction) {
//        return bankAccountRepository.findById(id).flatMap(bankAccount -> {
//            double total = bankAccount.getMonto();
//            if (total+bankAccountTransaction.getTransferenceAmount()>0){
//                bankAccount.setMonto(total+bankAccountTransaction.getTransferenceAmount());
//            }else{
//                return Mono.error(new Exception("Monto a retirar supera a monto actual"));
//            }
//            BankAccountTransaction newTransaction = new BankAccountTransaction();
//            newTransaction.setIdCliente(bankAccountTransaction.getIdCliente());
//            newTransaction.setSerialNumber(bankAccountTransaction.getSerialNumber());
//            newTransaction.setTransferenceType("TRANSFERENCE");
//            newTransaction.setTransferenceAmount(bankAccountTransaction.getTransferenceAmount());
//            newTransaction.setTotalAmount(bankAccount.getMonto());
//            bankAccountTransactionRepository.save(newTransaction).subscribe();
//            return bankAccountRepository.save(bankAccount);
//        });
//    }


    @Override
    public Mono<BankAccount> tranference(String id, BankAccountTransaction bankAccountTransaction) {
        return bankAccountRepository.findById(id)
                .filter(bankAccount -> bankAccount.getMonto() + bankAccountTransaction.getTransferenceAmount() >= 0)
                .switchIfEmpty(Mono.error(new Exception("Monto a retirar supera a monto actual")))
                .filter(bankAccount -> bankAccount.getMonto() +
                        bankAccountTransaction.getTransferenceAmount() >= bankAccount.getMinBalance())
                .switchIfEmpty(Mono.error(new Exception("Monto a retirar supera a monto permitido")))
                //.filter(bankAccount ->bankAccount.getTransactionLeft()>0)
                //.switchIfEmpty(Mono.error(new Exception("LIMITE DE TRANSACCIONES ALCANZADO")))

                .flatMap(bankAccount -> {
                    double descuento = bankAccount.getComision();
                    if (bankAccount.getTransactionLeft() > 0) {
                        bankAccount.setMonto(bankAccount.getMonto() + bankAccountTransaction.getTransferenceAmount());
                    } else {
                        bankAccount.setMonto(bankAccount.getMonto() + bankAccountTransaction.getTransferenceAmount() - descuento);
                    }
                    bankAccount.setTransactionLeft(bankAccount.getTransactionLeft() - 1);
                    BankAccountTransaction newTransaction = new BankAccountTransaction();
                    newTransaction.setIdCliente(bankAccount.getClientId());
                    newTransaction.setSerialNumber(bankAccount.getSerialNumber());
                    newTransaction.setTransferenceType("TRANSFERENCE");
                    newTransaction.setTransferenceAmount(bankAccountTransaction.getTransferenceAmount());
                    newTransaction.setTotalAmount(bankAccount.getMonto());
                    bankAccountTransactionRepository.save(newTransaction).subscribe();
                    System.out.println("MONTO INGRESADO:    " + newTransaction.getTransferenceAmount());
                    System.out.println("MONTO TOTAL ACTUAL: " + newTransaction.getTotalAmount());
                    System.out.println("LIMITE PARA RETIRAR:" + bankAccount.getMinBalance());
                    return bankAccountRepository.save(bankAccount);
                });
    }

    @Override
    public Mono<CreditPaymentRequest> tranferenceToCreditAcc(String id, CreditPaymentRequest creditPaymentRequest) {
        Mono<BankAccount> test = bankAccountRepository.findById(id)
                .filter(bankAccount ->bankAccount.getMonto()>0)
                .switchIfEmpty(Mono.error(new Exception("Monto a pagar debe ser mayor a 0")))
                .filter(bankAccount ->bankAccount.getMonto()-creditPaymentRequest.getAmmount()>=0)
                .switchIfEmpty(Mono.error(new Exception("Monto a pagar supera a monto en deuda")))
                .flatMap(bankAccount -> {
                    double descuento = bankAccount.getComision();
                    if (bankAccount.getTransactionLeft() > 0) {
                        bankAccount.setMonto(bankAccount.getMonto() + creditPaymentRequest.getAmmount());
                    } else {
                        bankAccount.setMonto(bankAccount.getMonto() + creditPaymentRequest.getAmmount() - descuento);
                    }
                    bankAccount.setTransactionLeft(bankAccount.getTransactionLeft() - 1);
                    BankAccountTransaction newTransaction = new BankAccountTransaction();
                    newTransaction.setIdCliente(bankAccount.getClientId());
                    newTransaction.setSerialNumber(bankAccount.getSerialNumber());
                    newTransaction.setTransferenceType("TRANSFERENCE");
                    newTransaction.setTransferenceAmount(creditPaymentRequest.getAmmount());
                    newTransaction.setTotalAmount(bankAccount.getMonto());
                    bankAccountTransactionRepository.save(newTransaction).subscribe();
                    System.out.println("MONTO INGRESADO:    " + newTransaction.getTransferenceAmount());
                    System.out.println("MONTO TOTAL ACTUAL: " + newTransaction.getTotalAmount());
                    System.out.println("LIMITE PARA RETIRAR:" + bankAccount.getMinBalance());
                    return bankAccountRepository.save(bankAccount);
                });
        return Mono.just(creditPaymentRequest);
    }
}
