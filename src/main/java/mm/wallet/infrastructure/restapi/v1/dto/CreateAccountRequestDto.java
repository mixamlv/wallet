package mm.wallet.infrastructure.restapi.v1.dto;

import lombok.Data;

import java.util.Currency;
import java.util.UUID;

@Data
public class CreateAccountRequestDto {
    private Currency currency;
    private UUID clientId;
}
