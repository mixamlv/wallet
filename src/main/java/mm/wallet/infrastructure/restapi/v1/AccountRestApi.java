package mm.wallet.infrastructure.restapi.v1;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import mm.wallet.domain.account.AccountService;
import mm.wallet.domain.account.transaction.TransactionService;
import mm.wallet.infrastructure.restapi.v1.dto.AccountDto;
import mm.wallet.infrastructure.restapi.v1.dto.CreateAccountRequestDto;
import mm.wallet.infrastructure.restapi.v1.dto.TransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("accounts")
@RequiredArgsConstructor
@Validated
public class AccountRestApi {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.POST)
    public AccountDto create(@RequestBody CreateAccountRequestDto req) {
        return AccountDto.dto(accountService.create(req.getCurrency(), req.getClientId()));
    }

    @RequestMapping(value = "{id}/transactions", method = RequestMethod.POST)
    public Page<TransactionDto> historyByAccount(@PathVariable("id") UUID id,
                                                 @RequestParam(name = "limit", defaultValue = "5") @Min(1) @Max(100) int limit,
                                                 @RequestParam(name = "offset", defaultValue = "0") int offset) {

        var pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "id"));
        return transactionService.historyByAccount(id, pageable).map(TransactionDto::dto);
    }

}
