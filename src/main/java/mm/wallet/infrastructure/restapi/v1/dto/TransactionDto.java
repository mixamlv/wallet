package mm.wallet.infrastructure.restapi.v1.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Data;
import mm.wallet.domain.account.Transaction;
import mm.wallet.domain.account.transaction.TransactionType;

@Data
public class TransactionDto {
    private Long id;
    private TransactionType type;
    private BigDecimal amount;

    public static TransactionDto dto(Transaction tx) {
        TransactionDto dto = new TransactionDto();
        dto.setId(tx.id());
        dto.setAmount(tx.amount().setScale(2, RoundingMode.HALF_UP));
        dto.setType(tx.type());
        return dto;
    }
}
