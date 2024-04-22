package mm.wallet.infrastructure.restapi.v1.dto;

import lombok.Data;
import mm.wallet.domain.account.Account;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Data
public class AccountDto {

    private UUID id;
    private Currency currency;
    private BigDecimal balance;

    public static AccountDto dtoShort(Account account) {
        AccountDto dto = new AccountDto();
        dto.setId(account.id());
        dto.setCurrency(account.currency());
        return dto;
    }

    public static AccountDto dto(Account account) {
        AccountDto dto = dtoShort(account);
        dto.setBalance(account.balance());
        return dto;
    }
}
