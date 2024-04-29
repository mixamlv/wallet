package mm.wallet.infrastructure.exchangerates;

import java.math.BigDecimal;
import java.util.Map;
import lombok.Data;

@Data
public class ExchangeRateResponse {
    private Boolean success;
    private Long timestamp;
    private String source;
    private Map<String, BigDecimal> quotes;
}
