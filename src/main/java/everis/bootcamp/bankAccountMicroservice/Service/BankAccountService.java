package everis.bootcamp.bankAccountMicroservice.Service;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccount;
import everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request.AddBankAccountRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankAccountService {
    Mono<BankAccount> create(AddBankAccountRequest addBankAccountRequest,String clientId);
    Mono<BankAccount> update(String id,AddBankAccountRequest addBankAccountRequest);
    Flux<BankAccount> readAll();
    Mono<BankAccount> delete(String bankId);
    Mono<BankAccount> getOne(String id);
}
