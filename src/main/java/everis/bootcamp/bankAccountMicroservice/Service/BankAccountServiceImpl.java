package everis.bootcamp.bankAccountMicroservice.Service;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccount;
import everis.bootcamp.bankAccountMicroservice.Repository.BankAccountRepository;
import everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request.AddBankAccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Override
    public Mono<BankAccount> create(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
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
    public Flux<BankAccount> readAll(String clientId) {
        return bankAccountRepository.findAllByClientId(clientId);
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

    @Override
    public Mono<BankAccount> isPresent(String clientId) {return bankAccountRepository.findByClientIdExists(clientId);
    }

}
