package mm.wallet.domain.client;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

public interface ClientService {

    Client create(String fullName);

    default Client get(UUID id) {
        return find(id).orElseThrow(() -> new EntityNotFoundException("Client not found by id: %s".formatted(id)));
    }

    Optional<Client> find(UUID id);
}
