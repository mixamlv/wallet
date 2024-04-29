package mm.wallet.domain.account;

import java.math.BigDecimal;
import lombok.Getter;
import mm.wallet.domain.core.DomainRuleException;

@Getter
public class InsufficientAccountFundsException extends DomainRuleException {
    private final BigDecimal amount;

    public InsufficientAccountFundsException(BigDecimal amount) {
        super("Insufficient Account funds");
        this.amount = amount;
    }
}
