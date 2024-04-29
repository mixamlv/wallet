package mm.wallet.domain.account.transfer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;
import mm.wallet.domain.account.Account;
import mm.wallet.domain.account.AccountService;
import mm.wallet.domain.account.Transaction;
import mm.wallet.domain.core.Money;
import mm.wallet.domain.exchange.CurrencyPair;
import mm.wallet.domain.exchange.Rate;
import mm.wallet.domain.exchange.RateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountTransferServiceImplTest {
    @Mock
    private RateRepository rateRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private AccountTransferRepository accountTransferRepository;

    @Mock
    private Account sourceAccount;
    @Mock
    private Account targetAccount;

    @Mock
    private Transaction sourceAccountTx;
    @Mock
    private Transaction targetAccountTx;
    @Mock
    private AccountTransfer transfer;

    private AccountTransferService service;
    private UUID sourceAccountId = UUID.fromString("a0a8a398-9f0c-4595-b5c4-2d036704e842");
    private UUID targetAccountId = UUID.fromString("4ed98c57-4fe4-497e-bbc5-10a059c558b9");

    @BeforeEach
    void setUp() {
        service = new AccountTransferServiceImpl(rateRepository, accountService, accountTransferRepository);
        when(accountService.find(sourceAccountId)).thenReturn(Optional.of(sourceAccount));
        when(accountService.withdraw(eq(sourceAccountId), any())).thenReturn(sourceAccountTx);
        when(accountService.deposit(eq(targetAccountId), any())).thenReturn(targetAccountTx);
        when(targetAccountTx.account()).thenReturn(targetAccount);
        when(sourceAccountTx.account()).thenReturn(sourceAccount);
        when(accountTransferRepository.save(any())).thenReturn(transfer);
    }

    @Test
    void whenAccountTransferWithSameCurrenciesCreated() {
        when(sourceAccount.currency()).thenReturn(Currency.getInstance("EUR"));
        when(targetAccount.currency()).thenReturn(Currency.getInstance("EUR"));
        var resultTransfer = service.create(sourceAccountId, targetAccountId, Money.money("10", "EUR"));
        assertNotNull(resultTransfer);
        assertEquals(transfer, resultTransfer);
        verifyNoInteractions(rateRepository);
    }

    @Test
    void whenAccountTransferWithExchangeCreated() {
        var pair = CurrencyPair.currencyPair("EUR", "USD");
        var rate = mock(Rate.class);
        when(rate.exchange(any(Money.class))).thenReturn(Money.money("11", "USD"));
        when(rateRepository.get(pair)).thenReturn(rate);
        when(sourceAccount.currency()).thenReturn(Currency.getInstance("USD"));
        when(targetAccount.currency()).thenReturn(Currency.getInstance("EUR"));
        var resultTransfer = service.create(sourceAccountId, targetAccountId, Money.money("10", "EUR"));
        assertNotNull(resultTransfer);
        assertEquals(transfer, resultTransfer);
        verify(rateRepository, times(1)).get(eq(pair));
    }
}