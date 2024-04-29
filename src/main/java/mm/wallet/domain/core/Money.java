package mm.wallet.domain.core;

import java.math.BigDecimal;
import java.util.Currency;


public record Money(BigDecimal amount, Currency currency) {
    public static Money money(String amount, String currencyCode) {
        return new Money(new BigDecimal(amount), Currency.getInstance(currencyCode));
    }

    public static Money money(BigDecimal amount, String currencyCode) {
        return new Money(amount, Currency.getInstance(currencyCode));
    }
}
