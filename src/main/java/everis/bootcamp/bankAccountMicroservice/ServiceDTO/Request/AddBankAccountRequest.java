package everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddBankAccountRequest {
    private String serialNumber;
    private String type;
    private String clientId;
}
