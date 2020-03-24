package everis.bootcamp.bankAccountMicroservice.Repository;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccountType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountTypeRepository extends ReactiveMongoRepository<BankAccountType,String> {
}
