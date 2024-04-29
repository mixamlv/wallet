package mm.wallet.domain.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class ClientTest {

    @Test
    void whenClientIsCreated() {
        var client = new Client("Janis Ozols");
        assertEquals("Janis Ozols", client.fullName());
        assertNotNull(client.id());
    }

}