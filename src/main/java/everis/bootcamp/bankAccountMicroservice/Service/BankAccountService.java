package everis.bootcamp.bankAccountMicroservice.Service;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccount;
import everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request.AddBankAccountRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankAccountService {
    Mono<BankAccount> create(BankAccount bankAccount);
    Mono<BankAccount> update(String id,AddBankAccountRequest addBankAccountRequest);
    Flux<BankAccount> readAll(String clientId);
    Mono<BankAccount> delete(String bankId);
    Mono<BankAccount> getOne(String id);
}
