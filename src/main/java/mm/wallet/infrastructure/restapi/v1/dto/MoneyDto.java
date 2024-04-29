package mm.wallet.infrastructure.restapi.v1.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Data;
import mm.wallet.domain.core.Money;

@Data
public class MoneyDto {
    private BigDecimal amount;
    private String currency;

    public static MoneyDto dto(Money money) {
        MoneyDto dto = new MoneyDto();
        dto.setAmount(money.amount().setScale(2, RoundingMode.HALF_UP));
        dto.setCurrency(money.currency().getCurrencyCode());
        return dto;
    }
}
