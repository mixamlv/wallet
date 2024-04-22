package mm.wallet.domain.account.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TransactionService {

    Page<Transaction> historyByAccount(UUID accountId, Pageable pageRequest);

}
