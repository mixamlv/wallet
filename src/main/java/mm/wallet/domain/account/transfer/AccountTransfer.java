package mm.wallet.domain.account.transfer;

import mm.wallet.domain.account.transaction.Transaction;
import mm.wallet.domain.common.Money;

import java.util.UUID;

public class AccountTransfer {

    private UUID id;
    private Transaction from;
    private Transaction to;
    private Money original;
    private Money actual;
}
