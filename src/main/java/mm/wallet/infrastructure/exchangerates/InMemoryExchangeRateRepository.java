package mm.wallet.infrastructure.exchangerates;

import static java.util.Optional.ofNullable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import mm.wallet.domain.exchange.CurrencyPair;
import mm.wallet.domain.exchange.Rate;
import mm.wallet.domain.exchange.RateRepository;

public class InMemoryExchangeRateRepository implements RateRepository {

    private final ConcurrentMap<CurrencyPair, Rate> rates = new ConcurrentHashMap<>();

    @Override
    public Optional<Rate> find(CurrencyPair pair) {
        return ofNullable(rates.get(pair))
                .or(() -> ofNullable(rates.get(pair.inverted())));
    }

    @Override
    public void update(Map<CurrencyPair, Rate> newRates) {
        rates.putAll(newRates);
    }
}
