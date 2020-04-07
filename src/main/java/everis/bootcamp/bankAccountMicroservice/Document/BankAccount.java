package everis.bootcamp.bankAccountMicroservice.Document;

import lombok.*;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Document(value = "bankAccount")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)

public class BankAccount {
    @Id
    private String id;
    @NotBlank(message = "'serialNumber' can't be blank")
    private String serialNumber;
    @NotNull(message = "'Type' can't be blank")
    private BankAccountType bankAccountType;
    @NotBlank(message = "'clientId' can't be blank")
    private String clientId;
    @NotBlank(message = "'dni' can't be blank")
    private String dni;
    @NotBlank(message = "'bankId' can't be blank")
    private String bankId;
    private double monto;
    private Set<String> holders;
    private Set<String> signers;
    private int transactionLeft;
    private double comision;
    private Date creationDate;
    /*<------------------------------------------------>*/
    /*  ESTOS CAMPOS SOLO PERTENECEN A LOS NUEVOS TIPOS */
    /*<------------------------------------------------>*/
    private double minBalance;
}
