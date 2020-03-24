package everis.bootcamp.bankAccountMicroservice.Service;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccountType;
import everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request.BankAccountTypeRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankAccountTypeService {
    Mono<BankAccountType> create(BankAccountType bankAccountType);
    Mono<BankAccountType> update(String id,BankAccountTypeRequest bankAccountTypeRequest);
    Flux<BankAccountType> readAll();
    Mono<BankAccountType> delete(String id);
    Mono<BankAccountType> getOne(String id);
}
