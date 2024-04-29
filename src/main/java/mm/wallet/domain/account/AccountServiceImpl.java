package mm.wallet.domain.account;

import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import mm.wallet.domain.client.ClientService;
import mm.wallet.domain.core.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientService clientService;

    @Override
    public Account create(Currency currency, UUID clientId) {
        var client = clientService.get(clientId);
        var account = new Account(currency);
        account.setClient(client);
        return accountRepository.save(account);
    }

    @Override
    public Optional<Account> find(UUID accountId) {
        return accountRepository.findById(accountId);
    }

    @Override
    public List<Account> listByClient(UUID clientId) {
        return accountRepository.findByClientId(clientId);
    }

    @Override
    @Transactional
    public Transaction withdraw(UUID accountId, Money money) {
        var account = accountRepository.findById(accountId).orElseThrow();
        var tx = account.withdraw(money);
        accountRepository.save(account);
        return account.transactions().getLast();
    }

    @Override
    @Transactional
    public Transaction deposit(UUID accountId, Money money) {
        var account = accountRepository.findById(accountId).orElseThrow();
        var tx = account.deposit(money);
        accountRepository.save(account);
        return account.transactions().getLast();
    }
}
