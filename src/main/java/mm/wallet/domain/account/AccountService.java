package mm.wallet.domain.account;

import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import mm.wallet.domain.core.Money;

public interface AccountService {

    Account create(Currency currency, UUID clientId);

    Optional<Account> find(UUID accountId);

    List<Account> listByClient(UUID clientId);

    Transaction withdraw(UUID accountId, Money money);

    Transaction deposit(UUID accountId, Money money);

}
