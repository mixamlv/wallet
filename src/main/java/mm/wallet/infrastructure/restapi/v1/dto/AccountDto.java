package mm.wallet.infrastructure.restapi.v1.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import lombok.Data;
import mm.wallet.domain.account.Account;
import mm.wallet.domain.client.Client;

@Data
public class AccountDto {

    private UUID id;
    private String currency;
    private BigDecimal balance;
    private UUID clientId;

    public static AccountDto dtoShort(Account account) {
        AccountDto dto = new AccountDto();
        dto.setId(account.id());
        dto.setCurrency(account.currency().getCurrencyCode());
        account.client().map(Client::id).ifPresent(dto::setClientId);
        return dto;
    }

    public static AccountDto dto(Account account) {
        AccountDto dto = dtoShort(account);
        dto.setBalance(account.balance().setScale(2, RoundingMode.HALF_UP));
        return dto;
    }
}
