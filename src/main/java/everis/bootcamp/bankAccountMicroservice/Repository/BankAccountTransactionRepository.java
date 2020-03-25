package everis.bootcamp.bankAccountMicroservice.Repository;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccountTransaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountTransactionRepository extends ReactiveMongoRepository<BankAccountTransaction,String> {
}
