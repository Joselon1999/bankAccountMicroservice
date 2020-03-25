package everis.bootcamp.bankAccountMicroservice.Controller;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccountTransaction;
import everis.bootcamp.bankAccountMicroservice.Document.BankAccountType;
import everis.bootcamp.bankAccountMicroservice.Service.BankAccountTransactionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class BanckAccountTransactionController {

    @Autowired
    BankAccountTransactionService bankAccountTransactionService;

    //READ
    @ApiOperation(value = "List all bankAccountTypes")
    @GetMapping(value = "/bankAccountTransactions")
    public ResponseEntity<Flux<BankAccountTransaction>> listBankAccountType(){
        return ResponseEntity.ok().body(bankAccountTransactionService.read());
    }
}
