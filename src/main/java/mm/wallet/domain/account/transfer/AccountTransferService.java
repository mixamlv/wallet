package mm.wallet.domain.account.transfer;

import java.util.UUID;
import mm.wallet.domain.core.Money;

public interface AccountTransferService {

    AccountTransfer create(UUID sourceAccountId, UUID targetAccountId, Money money);
}
