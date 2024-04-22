package mm.wallet.infrastructure.restapi.v1.dto;

import lombok.Data;
import mm.wallet.domain.account.transaction.Transaction;
import mm.wallet.domain.account.transaction.TransactionType;

import java.math.BigDecimal;

@Data
public class TransactionDto {
    private Long id;
    private TransactionType type;
    private BigDecimal amount;

    public static TransactionDto dto(Transaction tx) {
        TransactionDto dto = new TransactionDto();
        dto.setId(tx.id());
        dto.setAmount(tx.amount());
        dto.setType(tx.type());
        return dto;
    }
}
