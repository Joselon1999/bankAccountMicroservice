package everis.bootcamp.bankAccountMicroservice.Service;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccount;
import everis.bootcamp.bankAccountMicroservice.Repository.BankAccountRepository;
import everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request.AddBankAccountRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Override
    public Mono<BankAccount> create(AddBankAccountRequest addBankAccountRequest,String clientId) {
        return addBankAccountToRepository(addBankAccountRequest,clientId);
    }
    private Mono<BankAccount> addBankAccountToRepository(AddBankAccountRequest addBankAccountRequest,String clientId) {
        return bankAccountRepository.findBySerialNumber(addBankAccountRequest.getSerialNumber())
                .switchIfEmpty(bankAccountRepository.save(toBankAccount(addBankAccountRequest,clientId)));
    }

    private BankAccount toBankAccount(AddBankAccountRequest addBankAccountRequest,String clientId) {
        BankAccount bankAccount = new BankAccount();
        BeanUtils.copyProperties(addBankAccountRequest,bankAccount);
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setSerialNumber(addBankAccountRequest.getSerialNumber());
        bankAccount.setType(addBankAccountRequest.getType());
        bankAccount.setClientId(clientId);

        return bankAccount;
    }

    @Override
    public Mono<BankAccount> update(String id, AddBankAccountRequest addBankAccountRequest) {
        return bankAccountRepository.findById(id).flatMap(client -> {
            client.setSerialNumber(addBankAccountRequest.getSerialNumber());
            client.setType(addBankAccountRequest.getType());
            return bankAccountRepository.save(client);
        });

    }

    @Override
    public Flux<BankAccount> readAll() {
        return bankAccountRepository.findAll().switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<BankAccount> delete(String id) {
        return getOne(id).switchIfEmpty(Mono.empty()).filter(Objects::nonNull)
                .flatMap(bankAccount -> bankAccountRepository.delete(bankAccount).then(Mono.just(bankAccount)));
    }

    //Find One 100%
    @Override
    public Mono<BankAccount> getOne(String id) {
        return bankAccountRepository.findById(id);
    }

}
