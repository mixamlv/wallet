package mm.wallet.domain.exchange;

import java.util.List;
import java.util.Optional;

public interface RateRepository {

    List<Rate> all();

    Optional<Rate> get(CurrencyPair pair);
}
