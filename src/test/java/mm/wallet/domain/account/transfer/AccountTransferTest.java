package mm.wallet.domain.account.transfer;

import static mm.wallet.domain.account.transaction.TransactionType.DEPOSIT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.Currency;
import mm.wallet.domain.account.Account;
import mm.wallet.domain.account.Transaction;
import mm.wallet.domain.account.transaction.TransactionType;
import mm.wallet.domain.core.Money;
import org.junit.jupiter.api.Test;

class AccountTransferTest {

    public static Account mockAccount(Currency currency) {
        var account = mock(Account.class);
        when(account.currency()).thenReturn(currency);
        return account;
    }

    public static Transaction mockTx(Long id, TransactionType type, BigDecimal amount, String currencyCode) {
        var tx = mock(Transaction.class);
        var account = mock(Account.class);
        when(account.currency()).thenReturn(Currency.getInstance(currencyCode));
        when(tx.id()).thenReturn(id);
        when(tx.type()).thenReturn(type);
        when(tx.amount()).thenReturn(amount);
        when(tx.account()).thenReturn(account);
        return tx;
    }

    @Test
    void whenAccountTransferWithExchangeIsCreated() {
        // given
        var sourceAccountTx = mockTx(1L, DEPOSIT, new BigDecimal("2.10"), "EUR");
        var targetAccountTx = mockTx(1L, DEPOSIT, new BigDecimal("2.00"), "USD");
        var original = Money.money("2.10", "EUR");
        var actual = Money.money("2.00", "USD");
        // when
        var transfer = new AccountTransfer(sourceAccountTx, targetAccountTx, actual, original);
        // then
        assertEquals(actual, transfer.actualMoney());
        assertEquals(original, transfer.originalMoney());
        assertNotNull(transfer.createdAt());
        assertNotNull(transfer.id());
    }

    @Test
    void whenAccountTransferWithoutExchangeIsCreated() {
        // given
        var sourceAccountTx = mockTx(1L, DEPOSIT, new BigDecimal("5.00"), "EUR");
        var targetAccountTx = mockTx(1L, DEPOSIT, new BigDecimal("5.00"), "EUR");
        var money = Money.money("5.00", "EUR");
        // when
        var transfer = new AccountTransfer(sourceAccountTx, targetAccountTx, money);
        // then
        assertEquals(money, transfer.actualMoney());
        assertEquals(money, transfer.originalMoney());
        assertNotNull(transfer.createdAt());
        assertNotNull(transfer.id());
    }

    @Test
    void whenAccountTransferWithDifferentActualCurrency_Exception() {
        // given
        var sourceAccountTx = mockTx(1L, DEPOSIT, new BigDecimal("5.00"), "EUR");
        var targetAccountTx = mockTx(1L, DEPOSIT, new BigDecimal("5.00"), "EUR");
        var money = Money.money("5.00", "USD");
        // when then
        assertThrows(IllegalArgumentException.class, () -> new AccountTransfer(sourceAccountTx, targetAccountTx, money));
    }

    @Test
    void whenAccountTransferWithDifferentOriginalCurrency_Exception() {
        // given
        var sourceAccountTx = mockTx(1L, DEPOSIT, new BigDecimal("5.00"), "GBP");
        var targetAccountTx = mockTx(1L, DEPOSIT, new BigDecimal("5.10"), "EUR");
        var original = Money.money("5.10", "USD");
        var actual = Money.money("5.00", "EUR");
        // when then
        assertThrows(IllegalArgumentException.class, () -> new AccountTransfer(sourceAccountTx, targetAccountTx, actual, original));
    }
}