package everis.bootcamp.bankAccountMicroservice.Controller;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccountType;
import everis.bootcamp.bankAccountMicroservice.Service.BankAccountTypeService;
import everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request.BankAccountTypeRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/api")
public class BankAccountTypeController {

    @Autowired
    BankAccountTypeService bankAccountTypeService;

    //CREATE
    @ApiOperation(value = "Creates new bankAccountTypes")
    @PostMapping(value = "/bankAccountTypes")
    public Mono<BankAccountType> createBankAccountType(@Valid @RequestBody BankAccountType bankAccountType){
        return bankAccountTypeService.create(bankAccountType);
    }
    //UPDATE
    @ApiOperation(value = "Updates bankAccountTypes",
            notes = "Requires the bankAccountType ID and all BankAccountType Request Params - Which are same BankAccountType Params" +
                    "excluding the ID")
    @PutMapping(value = "/bankAccountTypes/{id}")
    public Mono<ResponseEntity<BankAccountType>> updateBankAccountType(@PathVariable("id") String id, @Valid @RequestBody BankAccountTypeRequest bankAccountTypeRequest) {
        return bankAccountTypeService.update(id,bankAccountTypeRequest)
                .map(bankAccountType -> ResponseEntity.created(URI.create("/bankAccountTypes".concat(bankAccountType.getId())))
                        .contentType(MediaType.APPLICATION_JSON).body(bankAccountType))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    //READ
    @ApiOperation(value = "List all bankAccountTypes")
    @GetMapping(value = "/bankAccountTypes")
    public ResponseEntity<Flux<BankAccountType>> listBankAccountType(){
        return ResponseEntity.ok().body(bankAccountTypeService.readAll());
    }
    //DELETE
    @ApiOperation(value = "Deletes a bankAccountType",
            notes = "Requires the bankAccountType ID")
    @DeleteMapping(value = "/bankAccountTypes/{bankAccountTypeId}")
    public Mono<BankAccountType> deleteBankAccountType(@PathVariable(value = "bankAccountTypeId") String bankAccountTypeId){
        return bankAccountTypeService.delete(bankAccountTypeId);
    }
    //FIND ONE
    @ApiOperation(value = "List one bankAccountType",
            notes = "Requires the bankAccountType ID")
    @GetMapping(value = "/bankAccountType/{bankAccountTypeId}")
    public Mono<BankAccountType> findOneBankAccountType(@PathVariable(value = "bankAccountTypeId") String bankAccountTypeId){
        return bankAccountTypeService.getOne(bankAccountTypeId);
    }
}
