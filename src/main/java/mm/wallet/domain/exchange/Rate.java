package mm.wallet.domain.exchange;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;


@RequiredArgsConstructor
class Rate {
    private final CurrencyPair pair;
    private final BigDecimal quote;
}
