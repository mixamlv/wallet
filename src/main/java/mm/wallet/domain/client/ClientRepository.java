package mm.wallet.domain.client;

import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

public interface ClientRepository extends ListCrudRepository<Client, UUID> {
}
