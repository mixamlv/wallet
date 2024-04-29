package mm.wallet.infrastructure.restapi.v1.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class CreateAccountRequestDto {
    private String currency;
    private UUID clientId;
}
