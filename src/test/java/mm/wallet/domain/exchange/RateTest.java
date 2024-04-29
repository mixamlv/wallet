package mm.wallet.domain.exchange;

import static mm.wallet.domain.core.Money.money;
import static mm.wallet.domain.exchange.CurrencyPair.currencyPair;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class RateTest {

    @Test
    void whenExchangeUsingBaseCurrency() {
        var rate = new Rate(currencyPair("EUR", "USD"), new BigDecimal("1.0723"));
        assertEquals(money("107.2300", "USD"), rate.exchange(money("100", "EUR")));
    }

    @Test
    void whenExchangeUsingTargetCurrency() {
        var rate = new Rate(currencyPair("EUR", "USD"), new BigDecimal("1.0723"));
        assertEquals(money("93.26", "EUR"), rate.exchange(money("100", "USD")));
    }

    @Test
    void whenExchangeUsingAnotherCurrency_Exception() {
        var rate = new Rate(currencyPair("EUR", "USD"), new BigDecimal("1.0723"));
        var ex = assertThrows(IllegalArgumentException.class, () -> rate.exchange(money("1", "GBP")));
        assertEquals("Money currency does not match base or target currency of exchange pair", ex.getMessage());
    }

    @Test
    void whenExchangeNonPositiveAmount_Exception() {
        var rate = new Rate(currencyPair("EUR", "USD"), new BigDecimal("1.0723"));
        var ex = assertThrows(IllegalArgumentException.class, () -> rate.exchange(money("0", "EUR")));
        assertEquals("Exchanged amount should be positive", ex.getMessage());
    }
}