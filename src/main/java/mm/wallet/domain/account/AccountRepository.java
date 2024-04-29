package mm.wallet.domain.account;

import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

public interface AccountRepository extends ListCrudRepository<Account, UUID> {

    List<Account> findByClientId(UUID clientId);
}
