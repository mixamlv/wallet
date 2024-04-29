package mm.wallet.infrastructure.restapi.v1.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class CreateAccountTransferRequestDto {
    private MoneyDto money;
    private UUID targetAccountId;
}
