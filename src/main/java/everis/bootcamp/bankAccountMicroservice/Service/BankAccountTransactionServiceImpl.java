package everis.bootcamp.bankAccountMicroservice.Service;

import everis.bootcamp.bankAccountMicroservice.Document.BankAccountTransaction;
import everis.bootcamp.bankAccountMicroservice.Repository.BankAccountTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BankAccountTransactionServiceImpl implements BankAccountTransactionService {

    @Autowired
    BankAccountTransactionRepository bankAccountTransactionRepository;
    @Override
    public Mono<BankAccountTransaction> create(BankAccountTransaction bankAccountTransaction) {
        return bankAccountTransactionRepository.save(bankAccountTransaction);
    }

    @Override
    public Mono<BankAccountTransaction> update(BankAccountTransaction bankAccountTransaction) {
        BankAccountTransaction newTransaction = new BankAccountTransaction();
            newTransaction.setIdCliente(bankAccountTransaction.getIdCliente());
            newTransaction.setSerialNumber(bankAccountTransaction.getSerialNumber());
            newTransaction.setTransferenceType("CREATING BUSINESS ACCOUNT");
            newTransaction.setTransferenceAmount(bankAccountTransaction.getTransferenceAmount());
            newTransaction.setTotalAmount(bankAccountTransaction.getTotalAmount());
            return bankAccountTransactionRepository.save(newTransaction);

    }

    @Override
    public Flux<BankAccountTransaction> read() {
        return bankAccountTransactionRepository.findAll();
    }
}
