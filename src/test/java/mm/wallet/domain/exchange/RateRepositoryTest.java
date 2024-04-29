package mm.wallet.domain.exchange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class RateRepositoryTest {
    private final RateRepository rateRepository = spy(RateRepository.class);

    @Test
    void whenRatePresent_Returned() {
        var rate = mock(Rate.class);
        var pair = CurrencyPair.parse("USDEUR");
        when(rateRepository.find(eq(pair))).thenReturn(Optional.of(rate));
        assertEquals(rate, rateRepository.get(pair));
    }

    @Test
    void whenRateNotFound_Exception() {
        when(rateRepository.find(any())).thenReturn(Optional.empty());
        assertThrows(ExchangeRateNotAvailableException.class, () -> rateRepository.get(CurrencyPair.parse("USDEUR")));
    }
}