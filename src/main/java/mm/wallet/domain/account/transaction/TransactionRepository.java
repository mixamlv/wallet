package mm.wallet.domain.account.transaction;

import java.util.List;
import java.util.UUID;
import mm.wallet.domain.account.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface TransactionRepository extends Repository<Transaction, Long> {

    @Query(value = "SELECT * FROM transaction as t WHERE t.account_id = ?1 ORDER BY t.id DESC LIMIT ?2 OFFSET ?3 ", nativeQuery = true)
    List<Transaction> findAllByAccountId(UUID accountId, long limit, long offset);
}
