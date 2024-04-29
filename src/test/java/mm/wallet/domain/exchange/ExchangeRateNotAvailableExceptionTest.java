package mm.wallet.domain.exchange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ExchangeRateNotAvailableExceptionTest {
    @Test
    void whenExceptionIsCreated() {
        var pair = CurrencyPair.parse("EURUSD");
        var ex = new ExchangeRateNotAvailableException(pair);
        assertEquals("Exchange rate not available for currency pair EURUSD", ex.getMessage());
    }
}