package mm.wallet.domain.account.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.UUID;
import mm.wallet.domain.account.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionHistoryServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    void whenHistoryByAccountIsFetched() {
        // given
        var service = new TransactionHistoryServiceImpl(transactionRepository);
        var tx = mock(Transaction.class);
        when(transactionRepository.findAllByAccountId(any(), anyLong(), anyLong())).thenReturn(List.of(tx));
        var accountId = UUID.fromString("a0a8a398-9f0c-4595-b5c4-2d036704e842");
        // when
        var response = service.historyByAccount(accountId, 10, 20);
        // then
        assertEquals(1, response.size());
        assertEquals(tx, response.getFirst());
        verify(transactionRepository, times(1)).findAllByAccountId(eq(accountId), eq(10L), eq(20L));

    }

}