package mm.wallet.domain.exchange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Currency;
import org.junit.jupiter.api.Test;

class CurrencyPairTest {

    @Test
    void whenCurrencyPairParsed() {
        var pair = CurrencyPair.parse("EURUSD");
        assertEquals(Currency.getInstance("EUR"), pair.base());
        assertEquals(Currency.getInstance("USD"), pair.target());
    }
}