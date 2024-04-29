package mm.wallet.domain.account.transfer;

import static com.google.common.base.Preconditions.checkArgument;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mm.wallet.domain.account.Transaction;
import mm.wallet.domain.core.Money;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountTransfer {

    @Id
    private UUID id = UUID.randomUUID();
    @OneToOne(cascade = {}, optional = false)
    @JoinColumn(name = "withdrawal_tx_id")
    private Transaction withdrawalTx;
    @OneToOne(cascade = {}, optional = false)
    @JoinColumn(name = "deposit_tx_id")
    private Transaction depositTx;
    private Currency originalCurrency;
    private Currency actualCurrency;
    private BigDecimal originalAmount;
    private BigDecimal actualAmount;
    private LocalDateTime createdAt = LocalDateTime.now();

    public AccountTransfer(Transaction sourceAccountTx, Transaction targetAccountTx, Money actual) {
        this(sourceAccountTx, targetAccountTx, actual, actual);
    }

    public AccountTransfer(Transaction sourceAccountTx, Transaction targetAccountTx, Money actual, Money original) {
        checkArgument(targetAccountTx.account().currency().equals(actual.currency()), "Actual money currency should be same as target account currency");
        checkArgument(sourceAccountTx.account().currency().equals(original.currency()), "Original money currency should be same as source account currency");
        this.withdrawalTx = sourceAccountTx;
        this.depositTx = targetAccountTx;
        this.actualAmount = actual.amount();
        this.actualCurrency = actual.currency();
        this.originalAmount = original.amount();
        this.originalCurrency = original.currency();
    }

    public UUID id() {
        return id;
    }

    public Money actualMoney() {
        return new Money(actualAmount, actualCurrency);
    }

    public Money originalMoney() {
        return new Money(originalAmount, originalCurrency);

    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public Transaction withdrawalTx() {
        return withdrawalTx;
    }

    public Transaction depositTx() {
        return depositTx;
    }
}
