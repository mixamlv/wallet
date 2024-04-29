package mm.wallet.infrastructure.exchangerates;

import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

public interface ExchangeRateClient {

    static ExchangeRateClient create(String baseUrl) {
        return Feign.builder()
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .target(ExchangeRateClient.class, baseUrl);
    }

    @RequestLine("GET /live?access_key={apiKey}&source={baseCurrency}")
    ExchangeRateResponse exchangeRates(@Param("apiKey") String apiKey, @Param("baseCurrency") String baseCurrency);

}
