package mm.wallet.infrastructure.restapi.v1.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import mm.wallet.domain.account.transfer.AccountTransfer;

@Data
public class AccountTransferDto {

    private UUID id;
    private MoneyDto actualMoney;
    private MoneyDto originalMoney;
    private LocalDateTime createdAt;

    public static AccountTransferDto dto(AccountTransfer transfer) {
        AccountTransferDto dto = new AccountTransferDto();
        dto.setId(transfer.id());
        dto.setActualMoney(MoneyDto.dto(transfer.actualMoney()));
        dto.setOriginalMoney(MoneyDto.dto(transfer.originalMoney()));
        dto.setCreatedAt(transfer.createdAt());
        return dto;
    }
}
