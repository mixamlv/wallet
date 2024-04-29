package mm.wallet.infrastructure.restapi.v1;

import static mm.wallet.infrastructure.restapi.v1.dto.TransactionDto.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import mm.wallet.domain.account.AccountService;
import mm.wallet.domain.account.transaction.TransactionHistoryService;
import mm.wallet.domain.account.transfer.AccountTransferService;
import mm.wallet.domain.core.Money;
import mm.wallet.infrastructure.restapi.v1.dto.AccountDto;
import mm.wallet.infrastructure.restapi.v1.dto.AccountTransferDto;
import mm.wallet.infrastructure.restapi.v1.dto.AddTransactionRequestDto;
import mm.wallet.infrastructure.restapi.v1.dto.CreateAccountRequestDto;
import mm.wallet.infrastructure.restapi.v1.dto.CreateAccountTransferRequestDto;
import mm.wallet.infrastructure.restapi.v1.dto.TransactionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
@Validated
public class AccountRestApi {

    private final AccountService accountService;
    private final AccountTransferService accountTransferService;
    private final TransactionHistoryService transactionService;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.POST)
    public AccountDto create(@RequestBody CreateAccountRequestDto req) {
        return AccountDto.dto(accountService.create(Currency.getInstance(req.getCurrency()), req.getClientId()));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<AccountDto> get(@PathVariable("id") UUID id) {
        return ResponseEntity.of(accountService.find(id).map(AccountDto::dto));
    }

    @RequestMapping(value = "{id}/transactions", method = RequestMethod.GET)
    public List<TransactionDto> historyByAccount(@PathVariable("id") UUID id,
                                                 @RequestParam(name = "limit", defaultValue = "10") @Min(1) @Max(100) long limit,
                                                 @RequestParam(name = "offset", defaultValue = "0") @Min(0) long offset) {
        return transactionService.historyByAccount(id, limit, offset).stream().map(TransactionDto::dto).toList();
    }

    @RequestMapping(value = "{id}/transactions", method = RequestMethod.POST)
    public TransactionDto addTransaction(@PathVariable("id") UUID id, @RequestBody AddTransactionRequestDto addTransactionRequest) {
        var money = Money.money(addTransactionRequest.getMoney().getAmount(), addTransactionRequest.getMoney().getCurrency());
        var tx = switch (addTransactionRequest.getType()) {
            case DEPOSIT -> accountService.deposit(id, money);
            case WITHDRAWAL -> accountService.withdraw(id, money);
        };
        return dto(tx);
    }

    @RequestMapping(value = "{id}/transfer", method = RequestMethod.POST)
    public AccountTransferDto transfer(@PathVariable("id") UUID id, @RequestBody CreateAccountTransferRequestDto createAccountTransferReq) {
        var money = Money.money(createAccountTransferReq.getMoney().getAmount(), createAccountTransferReq.getMoney().getCurrency());
        return AccountTransferDto.dto(accountTransferService.create(id, createAccountTransferReq.getTargetAccountId(), money));
    }
}
