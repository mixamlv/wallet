package mm.wallet.domain.exchange;

import java.util.Map;
import java.util.Optional;

public interface RateRepository {

    Optional<Rate> find(CurrencyPair pair);

    default Rate get(CurrencyPair pair) {
        return this.find(pair).orElseThrow(() -> new ExchangeRateNotAvailableException(pair));
    }

    void update(Map<CurrencyPair, Rate> newRates);
}
