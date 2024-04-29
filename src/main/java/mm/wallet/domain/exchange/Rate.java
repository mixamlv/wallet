package mm.wallet.domain.exchange;

import static com.google.common.base.Preconditions.checkArgument;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import mm.wallet.domain.core.Money;


@RequiredArgsConstructor
@EqualsAndHashCode
public class Rate {
    private final CurrencyPair pair;
    private final BigDecimal quote;

    public Money exchange(Money money) {
        checkArgument(pair.base().equals(money.currency()) || pair.target().equals(money.currency()), "Money currency does not match base or target currency of exchange pair");
        checkArgument(money.amount().compareTo(BigDecimal.ZERO) > 0, "Exchanged amount should be positive");

        BigDecimal exchangedAmount;
        if (pair.base().equals(money.currency())) {
            exchangedAmount = money.amount().multiply(quote);
            return new Money(exchangedAmount, pair.target());
        } else {
            exchangedAmount = money.amount().divide(quote, 2, RoundingMode.HALF_UP);
            return new Money(exchangedAmount, pair.base());
        }
    }

}
