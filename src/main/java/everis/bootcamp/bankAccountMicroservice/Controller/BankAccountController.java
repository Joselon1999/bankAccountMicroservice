package everis.bootcamp.bankAccountMicroservice.Controller;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccount;
import everis.bootcamp.bankAccountMicroservice.Document.BankAccountTransaction;
import everis.bootcamp.bankAccountMicroservice.Service.BankAccountService;
import everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request.AddBankAccountRequest;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/bankAccounts")
public class BankAccountController {

    @Autowired
    BankAccountService bankAccountService;

    @ApiOperation(value = "Creates new accounts",
            notes = "Requires a AddBankAccountRequest Params - Which are the same as  the bankAccount Params" +
            "excluding the ID")
    @PostMapping(value = "")
    public Mono<BankAccount> createClientBankAccount(@Valid @RequestBody AddBankAccountRequest addBankAccountRequest){
        return bankAccountService.create(addBankAccountRequest);
    }
/*UPDATE*/
    @ApiOperation(value = "Creates new accounts",
        notes = "Requires bankId and AddBankAccountRequest Params ")
    @PutMapping(value = "/{bankId}")
    public Mono<BankAccount> updateClientBankAccount(@PathVariable("bankId") String bankId,
                                                          @Valid @RequestBody AddBankAccountRequest addBankAccountRequest) {
        return bankAccountService.update(bankId,addBankAccountRequest);
    }
    /*READ*/
    @ApiOperation(value = "Creates new accounts",
            notes = "Requires Client ID")
    @GetMapping(value = "/{clientId}")
    public Flux<BankAccount> listClientBankAccounts(@PathVariable(value = "clientId") String clientId){
        return bankAccountService.readAll(clientId);
    }
    /*DELETE*/
    @ApiOperation(value = "Deletes a bank accounts",
            notes = "Requires BankAccount ID")
    @DeleteMapping(value = "/{bankId}")
    public Mono<BankAccount> deleteClientBankAccount(@PathVariable(value = "bankId") String bankId){
        return bankAccountService.delete(bankId);

    }
    /*FIND ONE*/
    @GetMapping(value = "/exist/{clientId}")
    public Mono<Boolean> findOne(@PathVariable(value = "clientId") String clientId){
        return bankAccountService.isPresent(clientId);
    }
    /*TRANSFERENCES*/
    @ApiOperation(value = "REGISTER TRANSFERENCE OF MONEY",
            notes = "Requires BANKACCONTRANSFERENCE and will update and create an entity")
    @PutMapping(value = "/transference/{id}")
    public Mono<BankAccount> transferenceBankAccount(@PathVariable(value = "id") String id,
                                                     @Valid @RequestBody BankAccountTransaction bankAccountTransaction) {
        return bankAccountService.tranference(id,bankAccountTransaction);
    }
}
