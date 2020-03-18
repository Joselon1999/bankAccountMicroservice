package everis.bootcamp.bankAccountMicroservice.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "BankAccountType")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountType {
    @Id
    private String id;
    private String tag;
    private String name;
}
