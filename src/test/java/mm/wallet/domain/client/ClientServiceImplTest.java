package mm.wallet.domain.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Test
    void whenClientIsCreatedUsingFullName() {
        // given
        var client = mock(Client.class);
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        var clientService = new ClientServiceImpl(clientRepository);
        // when
        var returnedClient = clientService.create("Josh Long");
        // then
        assertEquals(client, returnedClient);
        var captor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository, times(1)).save(captor.capture());
        assertEquals("Josh Long", captor.getValue().fullName());
    }

    @Test
    void whenClientIsFoundById() {
        // given
        var client = mock(Client.class);
        var id = UUID.fromString("a0a8a398-9f0c-4595-b5c4-2d036704e842");
        when(clientRepository.findById(eq(id))).thenReturn(Optional.of(client));
        var clientService = new ClientServiceImpl(clientRepository);
        // when
        var foundClient = clientService.find(id);
        // then
        assertTrue(foundClient.isPresent());
        var captor = ArgumentCaptor.forClass(UUID.class);
        verify(clientRepository, times(1)).findById(captor.capture());
        assertEquals(id, captor.getValue());
    }
}