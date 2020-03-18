package everis.bootcamp.bankAccountMicroservice.Service;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccount;
import everis.bootcamp.bankAccountMicroservice.Repository.BankAccountRepository;
import everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request.AddBankAccountRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    BankAccountRepository bankAccountRepository;
    /**/
    private Boolean hasBankAccount(String id){

        Boolean clientAccount = bankAccountRepository.existsByClientId(id).block();


        if (clientAccount == true) {
            System.out.println("TIENE CUENTA BANCARIA");
            return true;
        } else {
            System.out.println("NO POSEE CUENTA BANCARIA");
            return false;
        }
    }

    private Boolean existInClient(String id){
        String url = "http://localhost:8001/api/client/exist/"+id;
        Mono<Boolean> client= WebClient.create()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Boolean.class);

        Boolean value = client.block();
        if (value == true){
            System.out.println("ES CLIENTE");
        }else{
            System.out.println("NO ES CLIENTE");
        }
        return value;

    }

    @Override
    public Mono<BankAccount> create(AddBankAccountRequest addBankAccountRequest) {

        BankAccount bankAccount = new BankAccount();
        BeanUtils.copyProperties(addBankAccountRequest,bankAccount);
        bankAccount.setClientId(addBankAccountRequest.getClientId());
        bankAccount.setSerialNumber(addBankAccountRequest.getSerialNumber());
        bankAccount.setType(addBankAccountRequest.getType());
        if (hasBankAccount(bankAccount.getClientId())){
            System.out.println("1");
            return Mono.empty();
        }else{
        if (existInClient(bankAccount.getClientId())){
          System.out.println("2");
          return bankAccountRepository.save(bankAccount);
        }else {
            System.out.println("3");
            return Mono.empty();

            }
        }
    }

    @Override
    public Mono<BankAccount> update(String id, AddBankAccountRequest addBankAccountRequest) {
        return bankAccountRepository.findById(id).flatMap(client -> {
            client.setSerialNumber(addBankAccountRequest.getSerialNumber());
            client.setType(addBankAccountRequest.getType());
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


}
