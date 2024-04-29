package mm.wallet.domain.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class InsufficientAccountFundsExceptionTest {

    @Test
    void whenExceptionIsCreated() {
        var ex = new InsufficientAccountFundsException(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, ex.getAmount());
        assertEquals("Insufficient Account funds", ex.getMessage());
    }

}