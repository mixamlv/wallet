package mm.wallet.domain.exchange;

import static com.google.common.base.Preconditions.checkArgument;
import java.util.Currency;

public record CurrencyPair(Currency base, Currency target) {

    public static CurrencyPair currencyPair(String baseCurrencyCode, String targetCurrencyCode) {
        return new CurrencyPair(Currency.getInstance(baseCurrencyCode),
                Currency.getInstance(targetCurrencyCode));
    }

    public static CurrencyPair parse(String value) {
        checkArgument(value.length() == 6, "Currency pair should be 6 chars long");
        return currencyPair(value.substring(0, 3), value.substring(3, 6));
    }

    public CurrencyPair inverted() {
        return new CurrencyPair(this.target, this.base);
    }

    @Override
    public String toString() {
        return base.getCurrencyCode() + target.getCurrencyCode();
    }
}
