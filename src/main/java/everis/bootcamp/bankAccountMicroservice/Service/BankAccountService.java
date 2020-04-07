package everis.bootcamp.bankAccountMicroservice.Service;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccount;
import everis.bootcamp.bankAccountMicroservice.Document.BankAccountTransaction;
import everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request.AccountsRequest;
import everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request.AddBankAccountRequest;
import everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request.CreditPaymentRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankAccountService {
    Mono<BankAccount> create(AddBankAccountRequest addBankAccountRequest);
    Mono<BankAccount> update(String id,AddBankAccountRequest addBankAccountRequest);
    Flux<BankAccount> readAll(String clientId);
    Mono<BankAccount> delete(String bankId);
    Mono<BankAccount> getOne(String id);


    Mono<Boolean> isPresent(String clientId);

    Mono<BankAccount> tranference(String id, BankAccountTransaction bankAccountTransaction);
    Mono<CreditPaymentRequest> tranferenceToCreditAcc(String id, CreditPaymentRequest creditPaymentRequest);
    Flux<BankAccount> readAllByBankInTime(String bankId,int days);
}
