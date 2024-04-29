package mm.wallet.domain.account.transfer;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import mm.wallet.domain.account.AccountService;
import mm.wallet.domain.core.Money;
import mm.wallet.domain.exchange.CurrencyPair;
import mm.wallet.domain.exchange.RateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountTransferServiceImpl implements AccountTransferService {

    private final RateRepository rateRepository;
    private final AccountService accountService;
    private final AccountTransferRepository accountTransferRepository;

    @Override
    @Transactional
    public AccountTransfer create(UUID sourceAccountId, UUID targetAccountId, Money originalMoney) {
        var actualMoney = exchange(sourceAccountId, originalMoney);
        var withdrawalTx = accountService.withdraw(sourceAccountId, actualMoney);
        var depositTx = accountService.deposit(targetAccountId, originalMoney);
        var accountTransfer = new AccountTransfer(withdrawalTx, depositTx, originalMoney, actualMoney);
        return accountTransferRepository.save(accountTransfer);
    }

    private Money exchange(UUID sourceAccountId, Money originalMoney) {
        var sourceAccount = accountService.find(sourceAccountId).orElseThrow();
        if (sourceAccount.currency().equals(originalMoney.currency())) {
            return originalMoney;
        }
        var pair = new CurrencyPair(originalMoney.currency(), sourceAccount.currency());
        return rateRepository.get(pair).exchange(originalMoney);
    }
}
