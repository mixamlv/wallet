package mm.wallet.domain.account.transfer;

import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

public interface AccountTransferRepository extends ListCrudRepository<AccountTransfer, UUID> {
}
