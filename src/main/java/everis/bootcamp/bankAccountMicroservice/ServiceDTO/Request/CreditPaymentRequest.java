package everis.bootcamp.bankAccountMicroservice.ServiceDTO.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditPaymentRequest {
    private double ammount;
    private String idCreditAccount;
}
