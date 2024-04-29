package mm.wallet.infrastructure.restapi.v1.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;
import mm.wallet.domain.account.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountDtoTest {
    @Mock
    private Account account;

    @BeforeEach
    void setUp() {
        when(account.currency()).thenReturn(Currency.getInstance("EUR"));
        when(account.id()).thenReturn(UUID.fromString("a0a8a398-9f0c-4595-b5c4-2d036704e842"));
        when(account.client()).thenReturn(Optional.empty());
    }

    @Test
    void whenDtoShortIsMapped() {
        var dto = AccountDto.dtoShort(account);
        assertEquals("EUR", dto.getCurrency());
        assertEquals(UUID.fromString("a0a8a398-9f0c-4595-b5c4-2d036704e842"), dto.getId());
        assertNull(dto.getClientId());
    }

    @Test
    void whenDtoIsMapped() {
        when(account.balance()).thenReturn(BigDecimal.ONE);
        var dto = AccountDto.dto(account);
        assertEquals("EUR", dto.getCurrency());
        assertEquals(new BigDecimal("1.00"), dto.getBalance());
        assertEquals(UUID.fromString("a0a8a398-9f0c-4595-b5c4-2d036704e842"), dto.getId());
        assertNull(dto.getClientId());
    }
}