package mm.wallet.infrastructure.exchangerates;

import static java.util.stream.Collectors.toMap;
import java.util.Currency;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mm.wallet.domain.exchange.CurrencyPair;
import mm.wallet.domain.exchange.Rate;
import mm.wallet.domain.exchange.RateRepository;
import org.springframework.scheduling.annotation.Scheduled;


@RequiredArgsConstructor
@Slf4j
public class ExchangeRateUpdater {
    private final RateRepository repository;
    private final ExchangeRateClient exchangeRateClient;
    private final Currency baseCurrency;
    private final String apiKey;

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.HOURS)
    void update() {
        log.info("Updating rates started...");
        var rates = exchangeRateClient.exchangeRates(apiKey, baseCurrency.getCurrencyCode());
        if (rates.getQuotes() == null || rates.getQuotes().isEmpty()) {
            log.warn("No rates returned");
            return;
        }
        var newRates = rates.getQuotes().entrySet().stream()
                .collect(toMap(e -> CurrencyPair.parse(e.getKey()),
                        e -> new Rate(CurrencyPair.parse(e.getKey()), e.getValue())));
        repository.update(newRates);
        log.info("Updated rates for: %s".formatted(newRates.keySet()));
    }
}
