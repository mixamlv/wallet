package mm.wallet.infrastructure.restapi.v1.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mm.wallet.domain.account.transaction.TransactionType;

@Data
public class AddTransactionRequestDto {
    @NotNull
    private MoneyDto money;
    @NotNull
    private TransactionType type;
}
