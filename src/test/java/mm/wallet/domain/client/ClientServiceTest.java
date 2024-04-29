package mm.wallet.domain.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ClientServiceTest {

    private final ClientService clientService = spy(ClientService.class);

    @Test
    void whenClientPresent_Returned() {
        var client = mock(Client.class);
        var id = UUID.fromString("a0a8a398-9f0c-4595-b5c4-2d036704e842");
        when(clientService.find(eq(id))).thenReturn(Optional.of(client));
        assertEquals(client, clientService.get(id));
    }

    @Test
    void whenClientNotFound_Exception() {
        when(clientService.find(any())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> clientService.get(UUID.fromString("a0a8a398-9f0c-4595-b5c4-2d036704e842")));
    }
}