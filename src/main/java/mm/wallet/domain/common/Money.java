package mm.wallet.domain.common;

import java.math.BigDecimal;
import java.util.Currency;


public record Money(BigDecimal amount, Currency currency) {
}
