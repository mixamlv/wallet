package mm.wallet.infrastructure.exchangerates;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import mm.wallet.domain.exchange.CurrencyPair;
import mm.wallet.domain.exchange.Rate;
import org.junit.jupiter.api.Test;

class InMemoryExchangeRateRepositoryTest {

    @Test
    void whenEmptyRepository_EmptyResult() {
        var repo = new InMemoryExchangeRateRepository();
        assertEquals(Optional.empty(), repo.find(CurrencyPair.parse("USDEUR")));
    }

    @Test
    void whenRepositoryUpdate_RateFound() {
        // given
        var repo = new InMemoryExchangeRateRepository();
        var usdEurPair = CurrencyPair.parse("USDEUR");
        var usdEurRate = new Rate(usdEurPair, new BigDecimal("1.2001"));
        // when
        repo.update(Map.of(usdEurPair, usdEurRate));
        // then
        assertEquals(Optional.of(usdEurRate), repo.find(CurrencyPair.parse("USDEUR")));
        assertEquals(Optional.of(usdEurRate), repo.find(CurrencyPair.parse("EURUSD")));
        assertEquals(Optional.empty(), repo.find(CurrencyPair.parse("EURGBP")));
        assertEquals(Optional.empty(), repo.find(CurrencyPair.parse("EURCHF")));
    }

}