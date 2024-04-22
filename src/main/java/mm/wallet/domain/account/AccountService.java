package mm.wallet.domain.account;

import java.util.Currency;
import java.util.List;
import java.util.UUID;

public interface AccountService {

    Account create(Currency currency, UUID clientId);

    List<Account> listByClient(UUID clientId);
}
