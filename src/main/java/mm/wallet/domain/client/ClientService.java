package mm.wallet.domain.client;

import java.util.UUID;

public interface ClientService {

    Client create(String username);

    Client get(UUID id);
}
