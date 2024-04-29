package mm.wallet.domain.account.transaction;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import mm.wallet.domain.account.Transaction;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

    private final TransactionRepository transactionRepository;

    @Override
    public List<Transaction> historyByAccount(UUID accountId, long limit, long offset) {
        return transactionRepository.findAllByAccountId(accountId, limit, offset);
    }
}
