package mm.wallet.domain.account;

import static java.math.BigDecimal.ZERO;
import static mm.wallet.domain.core.Money.money;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Currency;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AccountTest {

    @Test
    void whenNewAccountCreated() {
        var account = new Account(Currency.getInstance("EUR"));
        assertEquals(ZERO, account.balance());
        assertEquals(money("0", "EUR"), account.balanceMoney());
        assertEquals(Currency.getInstance("EUR"), account.currency());
        assertEquals(Optional.empty(), account.client());
        assertNotNull(account.id());
    }

    @Test
    void whenNewAccountBalanceNotEnoughForDeposit_InsufficientAccountFundsException() {
        var account = new Account(Currency.getInstance("EUR"));
        assertThrows(InsufficientAccountFundsException.class, () -> account.withdraw(money("1.00", "EUR")));
    }

    @Test
    void whenNegativeDeposit_Exception() {
        var account = new Account(Currency.getInstance("EUR"));
        assertThrows(IllegalArgumentException.class, () -> account.deposit(money("-1", "EUR")));
    }

    @Test
    void whenNegativeWithdrawal_Exception() {
        var account = new Account(Currency.getInstance("EUR"));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(money("-1", "EUR")));
    }

    @Test
    void whenWithdrawalInDifferentCurrency_Exception() {
        var account = new Account(Currency.getInstance("EUR"));
        account.deposit(money("5.00", "EUR"));
        var ex = assertThrows(IllegalArgumentException.class, () -> account.withdraw(money("1", "USD")));
        assertEquals("Withdraw should be in account currency", ex.getMessage());
    }

    @Test
    void whenDepositInDifferentCurrency_Exception() {
        var account = new Account(Currency.getInstance("EUR"));
        var ex = assertThrows(IllegalArgumentException.class, () -> account.deposit(money("1", "USD")));
        assertEquals("Deposit should be in account currency", ex.getMessage());
    }


    @Test
    void whenNewAccountBalanceEnoughForDeposit_BalanceIsChanged() {
        // given
        var account = new Account(Currency.getInstance("EUR"));
        account.deposit(money("5.00", "EUR"));
        assertEquals(money("5.00", "EUR"), account.balanceMoney());
        // when
        account.withdraw(money("1.33", "EUR"));
        // then
        assertEquals(money("3.67", "EUR"), account.balanceMoney());
    }
}