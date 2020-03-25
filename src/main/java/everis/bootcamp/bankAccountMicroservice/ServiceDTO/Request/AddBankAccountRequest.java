package everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class AddBankAccountRequest {
    private String serialNumber;
    private String idBankAccountType;
    private String clientId;
    private String dni;
    private double monto;
    private Set<String> holders;
    private Set<String> signers;
}
