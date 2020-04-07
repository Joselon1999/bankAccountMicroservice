package everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountsRequest {
    private String id;
    private String serialNumber;
    private String idBankAccountType;
    private String clientId;
    private String dni;
    private String bankId;
    private double ammount;
}
