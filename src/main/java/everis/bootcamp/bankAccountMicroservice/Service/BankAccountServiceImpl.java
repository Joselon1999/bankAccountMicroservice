package everis.bootcamp.bankAccountMicroservice.Service;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccount;
import everis.bootcamp.bankAccountMicroservice.Repository.BankAccountRepository;
import everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request.AddBankAccountRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    BankAccountRepository bankAccountRepository;

    private Boolean exist(String id){

        Mono<Boolean> temp = Mono.just(true);

        //MAP INSIDE COLLECTION BANK ACCOUNT
        /*Mono<Boolean> inBankAccount = bankAccountService.isPresent(id);
        if (inBankAccount.equals(temp)){
            System.out.println("Existe  ");
        }else {
            System.out.println("No existe  ");
        }*/

        //MAP OUTSIDE ON COLLECTION CLIENTS
        Map<String,String> uriVariables= new HashMap<>();
        uriVariables.put("clientId",id);
        ResponseEntity<Boolean> responseEntity =new RestTemplate().
                getForEntity("http://localhost:8001/api/clients/exist/{clientId}",
                        Boolean.class,uriVariables);
        Boolean isPresent = responseEntity.getBody();
        return isPresent;
    }

    @Override
    public Mono<BankAccount> create(AddBankAccountRequest addBankAccountRequest) {

        BankAccount bankAccount = new BankAccount();
        BeanUtils.copyProperties(addBankAccountRequest,bankAccount);
        bankAccount.setClientId(addBankAccountRequest.getClientId());
        bankAccount.setSerialNumber(addBankAccountRequest.getSerialNumber());
        bankAccount.setType(addBankAccountRequest.getType());
        if (exist(bankAccount.getClientId())){
            return bankAccountRepository.save(bankAccount);
        }else {
            return Mono.empty();
        }
    }

    @Override
    public Mono<BankAccount> update(String id, AddBankAccountRequest addBankAccountRequest) {
        return bankAccountRepository.findById(id).flatMap(client -> {
            client.setSerialNumber(addBankAccountRequest.getSerialNumber());
            client.setType(addBankAccountRequest.getType());
            client.setClientId(addBankAccountRequest.getClientId());
            return bankAccountRepository.save(client);
        });

    }

    @Override
    public Flux<BankAccount> readAll(String clientId) {
        if (exist(clientId)) {
            return bankAccountRepository.findAllByClientId(clientId);
        }else{
            return null;
        }
    }

    @Override
    public Mono<BankAccount> delete(String id) {
        return getOne(id).switchIfEmpty(Mono.empty()).filter(Objects::nonNull)
                .flatMap(bankAccount -> bankAccountRepository.delete(bankAccount).then(Mono.just(bankAccount)));
    }

    //Find One 100%
    @Override
    public Mono<BankAccount> getOne(String id) {
        return bankAccountRepository.findById(id);
    }

    @Override
    public Mono<BankAccount> isPresent(String clientId) {return bankAccountRepository.findByClientIdExists(clientId);
    }

}
