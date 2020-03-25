package everis.bootcamp.bankAccountMicroservice.Repository;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount,String> {
    Flux<BankAccount> findAllByClientId(String clientId);
    Mono<BankAccount> findByClientId(String clientId);
    Mono<Boolean> existsByClientId(String clientId);
    Flux<BankAccount> findAllByBankAccountType_Id(String Id);
}
