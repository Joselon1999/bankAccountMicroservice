package everis.bootcamp.bankAccountMicroservice.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Document(value = "bankAccount")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {
    @Id
    private String id;
    @NotBlank(message = "'serialNumber' can't be blank")
    private String serialNumber;
    @NotNull(message = "'Type' can't be blank")
    private BankAccountType bankAccountType;
    @NotBlank(message = "'clientId' can't be blank")
    private String clientId;
}
