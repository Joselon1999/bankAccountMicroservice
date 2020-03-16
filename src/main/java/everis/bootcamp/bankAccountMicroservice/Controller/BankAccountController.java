package everis.bootcamp.bankAccountMicroservice.Controller;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccount;
import everis.bootcamp.bankAccountMicroservice.Service.BankAccountService;
import everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request.AddBankAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/bankAccounts")
public class BankAccountController {

    @Autowired
    BankAccountService bankAccountService;

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

    @PostMapping(value = "/newBankAccount/{clientId}")
    public Mono<BankAccount> createClientBankAccount(@PathVariable(value = "clientId") String clientId,
                                               @RequestBody AddBankAccountRequest addBankAccountRequest){
        BankAccount bankAccount = new BankAccount();
        BeanUtils.copyProperties(addBankAccountRequest,bankAccount);
        bankAccount.setClientId(clientId);
        bankAccount.setSerialNumber(addBankAccountRequest.getSerialNumber());
        bankAccount.setType(addBankAccountRequest.getType());
        if (exist(clientId)){
            return bankAccountService.create(bankAccount);
        }else {
            return Mono.empty();
        }
    }
/*UPDATE*/

    @PutMapping(value = "/update/{clientId}/{bankId}")
    public Mono<ResponseEntity<BankAccount>> updateClientBankAccount(@PathVariable("clientId") String clientId,
                                                          @PathVariable("bankId") String bankId,
                                                          @RequestBody AddBankAccountRequest addBankAccountRequest) {
        return bankAccountService.update(bankId,addBankAccountRequest)
                .map(bankAccount -> ResponseEntity.created(URI.create("/bankAccounts".concat(bankAccount.getId())))
                        .contentType(MediaType.APPLICATION_JSON).body(bankAccount))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    /*READ*/
    @GetMapping(value = "/selectAll/{clientId}")
    public ResponseEntity<Flux<BankAccount>> listClientBankAccounts(@PathVariable(value = "clientId") String clientId){
        if (exist(clientId)) {
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(bankAccountService.readAll(clientId));
        }else{
            return null;
        }
    }
    /*DELETE*/
    @DeleteMapping(value = "/delete/{clientId}/{bankId}")
    public Mono<BankAccount> deleteClientBankAccount(@PathVariable(value = "clientId") String clientId,
                                          @PathVariable(value = "bankId") String bankId){
        if (exist(clientId)) {
        return bankAccountService.delete(bankId);
        }else{
            return Mono.empty();
        }
    }
    /*FIND ONE
    @GetMapping(value = "/find/{clientId}")
    public Mono<Client> findOne(@PathVariable(value = "clientId") String clientId){
        return clientService.getOne(clientId);
    }*/
}
