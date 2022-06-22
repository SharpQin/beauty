package cc.microthink.common.dto.customer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountDTO {

    private Long id;

    private String name;

    private String cardNo;

    private AccountType accountType;

    public enum AccountType {
        BANK,
        CREDIT,
    }

    public AccountDTO(Long id, String name, String cardNo, AccountType accountType) {
        this.id = id;
        this.name = name;
        this.cardNo = cardNo;
        this.accountType = accountType;
    }
}
