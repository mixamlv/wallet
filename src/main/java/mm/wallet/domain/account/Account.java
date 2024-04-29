package mm.wallet.domain.account;

import static com.google.common.base.Preconditions.checkArgument;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mm.wallet.domain.account.transaction.TransactionType;
import mm.wallet.domain.client.Client;
import mm.wallet.domain.core.Money;
import org.hibernate.annotations.BatchSize;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Account implements Serializable {
    @Id
    private UUID id = UUID.randomUUID();
    @Version
    int version;
    @Setter
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    private BigDecimal balance = BigDecimal.ZERO;
    private Currency currency;
    @OrderBy(value = "id")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account", orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size = 50)
    private List<Transaction> transactions = new ArrayList<>();

    public Account(Currency currency) {
        this.currency = currency;
    }

    public UUID id() {
        return id;
    }

    public Optional<Client> client() {
        return Optional.ofNullable(client);
    }

    public BigDecimal balance() {
        return balance;
    }

    public Money balanceMoney() {
        return new Money(balance, this.currency);
    }

    public Currency currency() {
        return currency;
    }

    public Transaction withdraw(Money money) {
        checkArgument(money.currency().equals(currency), "Withdraw should be in account currency");
        checkArgument(money.amount().compareTo(BigDecimal.ZERO) > 0, "Withdraw amount should not be negative");
        if (balance.compareTo(money.amount()) <= 0) {
            throw new InsufficientAccountFundsException(money.amount());
        }
        balance = balance.subtract(money.amount());
        var tx = new Transaction(money.amount(), TransactionType.WITHDRAWAL, this);
        transactions.add(tx);
        return tx;
    }

    public Transaction deposit(Money money) {
        checkArgument(money.currency().equals(currency), "Deposit should be in account currency");
        checkArgument(money.amount().compareTo(BigDecimal.ZERO) >= 0, "Deposit amount should not be negative");
        var tx = new Transaction(money.amount(), TransactionType.DEPOSIT, this);
        balance = balance.add(money.amount());
        transactions.add(tx);
        return tx;
    }

    List<Transaction> transactions() {
        return Collections.unmodifiableList(transactions);
    }
}
