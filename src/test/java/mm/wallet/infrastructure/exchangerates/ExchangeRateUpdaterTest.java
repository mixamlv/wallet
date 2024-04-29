package mm.wallet.infrastructure.exchangerates;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import mm.wallet.domain.exchange.CurrencyPair;
import mm.wallet.domain.exchange.Rate;
import mm.wallet.domain.exchange.RateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExchangeRateUpdaterTest {

    @Mock
    private RateRepository repository;
    @Mock
    private ExchangeRateClient client;

    private ExchangeRateUpdater rateUpdater;

    @BeforeEach
    void setUp() {
        rateUpdater = new ExchangeRateUpdater(repository, client, Currency.getInstance("EUR"), "apyKey");
    }

    @Test
    void whenRatesArePresent_RatesUpdated() {
        when(client.exchangeRates(any(), eq("EUR"))).thenReturn(new ExchangeRateResponse());
        rateUpdater.update();
        verifyNoInteractions(repository);
    }

    @Test
    void whenRatesAreMissing_RatesNotUpdated() {
        var response = mock(ExchangeRateResponse.class);
        var pair = CurrencyPair.parse("EURUSD");
        when(response.getQuotes()).thenReturn(Map.of(pair.toString(), new BigDecimal("1.03")));
        when(client.exchangeRates(any(), eq("EUR"))).thenReturn(response);
        rateUpdater.update();
        verify(repository, times(1)).update(
                eq(Map.of(pair, new Rate(pair, new BigDecimal("1.03")))));
    }
}