
package everis.bootcamp.bankAccountMicroservice.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bankAccountTransaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountTransaction {
    private String id;
    private String serialNumber;
    private String idCliente;
    private String transferenceType;
    private double transferenceAmount;
    private double totalAmount;
}
