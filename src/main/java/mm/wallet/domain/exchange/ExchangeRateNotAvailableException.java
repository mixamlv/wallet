package mm.wallet.domain.exchange;

import lombok.Getter;
import mm.wallet.domain.core.DomainRuleException;

@Getter
public class ExchangeRateNotAvailableException extends DomainRuleException {
    private final CurrencyPair pair;

    public ExchangeRateNotAvailableException(CurrencyPair pair) {
        super("Exchange rate not available for currency pair %s".formatted(pair));
        this.pair = pair;
    }
}

