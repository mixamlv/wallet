package mm.wallet.domain.account.transaction;

import java.util.List;
import java.util.UUID;
import mm.wallet.domain.account.Transaction;

public interface TransactionHistoryService {

    List<Transaction> historyByAccount(UUID accountId, long limit, long offset);
}
