package everis.bootcamp.bankAccountMicroservice.Repository;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount,String> {
    Mono<BankAccount> findBySerialNumber(String serialNumber);
}