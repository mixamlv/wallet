package mm.wallet.domain.account;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends CrudRepository<Account, UUID> {

    List<Account> findByClientId(UUID clientId);
}
