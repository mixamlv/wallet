package mm.wallet.domain.account;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Accessors(fluent = true)
public class Account {
    private UUID id = UUID.randomUUID();
    @Setter
    private UUID clientId;
    private BigDecimal balance = BigDecimal.ZERO;
    private Currency currency;

    public Account(Currency currency) {
        this.currency = currency;
    }

}
