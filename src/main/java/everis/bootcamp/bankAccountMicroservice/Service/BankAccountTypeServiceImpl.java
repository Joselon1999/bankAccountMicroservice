package everis.bootcamp.bankAccountMicroservice.Service;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccountType;
import everis.bootcamp.bankAccountMicroservice.Repository.BankAccountTypeRepository;
import everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request.BankAccountTypeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class BankAccountTypeServiceImpl implements BankAccountTypeService{

    @Autowired
    BankAccountTypeRepository bankAccountTypeRepository;

    @Override
    public Mono<BankAccountType> create(BankAccountType bankAccountType) {
        return bankAccountTypeRepository.save(bankAccountType);
    }

    @Override
    public Mono<BankAccountType> update(String id, BankAccountTypeRequest bankAccountTypeRequest) {
        return bankAccountTypeRepository.findById(id).flatMap(bankAccount -> {
            bankAccount.setName(bankAccountTypeRequest.getName());
            return bankAccountTypeRepository.save(bankAccount);
        });
    }

    @Override
    public Flux<BankAccountType> readAll() {
        return bankAccountTypeRepository.findAll();
    }

    @Override
    public Mono<BankAccountType> delete(String id) {
        return getOne(id).switchIfEmpty(Mono.empty()).filter(Objects::nonNull)
                .flatMap(bankAccountType -> bankAccountTypeRepository.delete(bankAccountType).then(Mono.just(bankAccountType)));
    }

    @Override
    public Mono<BankAccountType> getOne(String id) {
        return bankAccountTypeRepository.findById(id);
    }
}
