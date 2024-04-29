package mm.wallet.domain.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import mm.wallet.domain.client.ClientService;
import mm.wallet.domain.core.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ClientService clientService;
    @Mock
    private Account account;
    private AccountService service;

    @BeforeEach
    void setUp() {
        service = new AccountServiceImpl(accountRepository, clientService);
    }

    @Test
    void whenAccountCreated() {
        var clientId = UUID.fromString("a0a8a398-9f0c-4595-b5c4-2d036704e842");
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        var resultAccount = service.create(Currency.getInstance("EUR"), clientId);
        assertEquals(account, resultAccount);
    }

    @Test
    void whenAccountFoundById() {
        var accountId = UUID.fromString("a0a8a398-9f0c-4595-b5c4-2d036704e842");
        when(accountRepository.findById(eq(accountId))).thenReturn(Optional.of(account));
        var accountOptional = service.find(accountId);
        assertEquals(Optional.of(account), accountOptional);
    }

    @Test
    void whenAccountsListedByClient() {
        var clientId = UUID.fromString("a0a8a398-9f0c-4595-b5c4-2d036704e842");
        when(accountRepository.findByClientId(clientId)).thenReturn(List.of(account));
        var clientAccounts = service.listByClient(clientId);
        assertEquals(List.of(account), clientAccounts);
    }

    @Test
    void whenWithdrawFromAccount() {
        var accountId = UUID.fromString("a0a8a398-9f0c-4595-b5c4-2d036704e842");
        when(accountRepository.findById(eq(accountId))).thenReturn(Optional.of(account));
        var money = Money.money("5", "EUR");
        var tx = mock(Transaction.class);
        when(account.withdraw(eq(money))).thenReturn(tx);
        when(account.transactions()).thenReturn(List.of(tx));
        var resultTx = service.withdraw(accountId, money);
        assertEquals(tx, resultTx);
        verify(account, times(1)).withdraw(eq(money));
    }

    @Test
    void whenDepositToAccount() {
        var accountId = UUID.fromString("a0a8a398-9f0c-4595-b5c4-2d036704e842");
        when(accountRepository.findById(eq(accountId))).thenReturn(Optional.of(account));
        var money = Money.money("5", "EUR");
        var tx = mock(Transaction.class);
        when(account.deposit(eq(money))).thenReturn(tx);
        when(account.transactions()).thenReturn(List.of(tx));
        var resultTx = service.deposit(accountId, money);
        assertEquals(tx, resultTx);
        verify(account, times(1)).deposit(eq(money));
    }
}