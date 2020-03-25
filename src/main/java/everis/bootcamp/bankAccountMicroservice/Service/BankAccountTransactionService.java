package everis.bootcamp.bankAccountMicroservice.Service;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccountTransaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankAccountTransactionService {
    Mono<BankAccountTransaction> create(BankAccountTransaction bankAccountTransaction);
    Mono<BankAccountTransaction> update(BankAccountTransaction bankAccountTransaction);
    Flux<BankAccountTransaction> read();
}
