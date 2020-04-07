package everis.bootcamp.bankAccountMicroservice;

import everis.bootcamp.bankAccountMicroservice.Controller.BankAccountController;
import everis.bootcamp.bankAccountMicroservice.Document.BankAccount;
import everis.bootcamp.bankAccountMicroservice.Document.BankAccountType;
import everis.bootcamp.bankAccountMicroservice.Repository.BankAccountRepository;
import everis.bootcamp.bankAccountMicroservice.Service.BankAccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.HashSet;

import static org.mockito.Mockito.times;

//@SpringBootTest
@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = BankAccountController.class)
@Import(BankAccountService.class)
class BankAccountMicroserviceApplicationTests {

    @Test
    void contextLoads() {
    }

    @MockBean
    BankAccountRepository repository;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testCreateBankAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setClientId("1");
        bankAccount.setSerialNumber("123123123");
        bankAccount.setDni("11111111");
        bankAccount.setMonto(10000);
        bankAccount.setComision(100);
        bankAccount.setHolders(new HashSet<>());
        bankAccount.setSigners(new HashSet<>());
        bankAccount.setTransactionLeft(5);
        bankAccount.setBankId("1");
        bankAccount.setBankAccountType(new BankAccountType("1", "TYPE", 1000));

        Mockito.when(repository.save(bankAccount)).thenReturn(Mono.just(bankAccount));

        webTestClient.post()
                .uri("/api/bankAccounts")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(bankAccount))
                .exchange()
                .expectStatus().isCreated();

        Mockito.verify(repository, times(1)).save(bankAccount);
    }

}
