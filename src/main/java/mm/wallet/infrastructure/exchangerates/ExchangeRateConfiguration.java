package mm.wallet.infrastructure.exchangerates;

import java.util.Currency;
import mm.wallet.domain.exchange.RateRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ExchangeRateConfiguration {

    @Value("${infra.exchangerate.baseUrl:http://localhost:1080}")
    private String baseUrl;
    @Value("${infra.exchangerate.apiKey:}")
    private String apiKey;
    @Value("${infra.exchangerate.bseCurrency:USD}")
    private String bseCurrency;

    @Bean
    public RateRepository inMemoryExchangeRateRepository() {
        return new InMemoryExchangeRateRepository();
    }

    @Bean
    public ExchangeRateClient exchangeRateClient() {
        return ExchangeRateClient.create(baseUrl);
    }

    @Bean
    public ExchangeRateUpdater exchangeRateUpdater() {
        return new ExchangeRateUpdater(inMemoryExchangeRateRepository(), exchangeRateClient(),
                Currency.getInstance(bseCurrency), apiKey);
    }

}
