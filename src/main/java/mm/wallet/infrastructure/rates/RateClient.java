package mm.wallet.infrastructure.rates;

import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.Response;

public interface RateClient {

    static RateClient create(String baseUrl) {
        return Feign.builder()
                .target(RateClient.class, baseUrl);
    }

    @RequestLine("GET /live?access_key={apiKey}&source={baseCurrency}")
    Response exchangeRates(@Param("apiKey") String apiKey, @Param("baseCurrency") String baseCurrency);

}
