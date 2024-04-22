package mm.wallet.infrastructure.restapi.v1;

import lombok.RequiredArgsConstructor;
import mm.wallet.domain.account.AccountService;
import mm.wallet.domain.client.ClientService;
import mm.wallet.infrastructure.restapi.v1.dto.AccountDto;
import mm.wallet.infrastructure.restapi.v1.dto.ClientCreateRequestDto;
import mm.wallet.infrastructure.restapi.v1.dto.ClientDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static mm.wallet.infrastructure.restapi.v1.dto.ClientDto.dto;

@RestController("clients")
@RequiredArgsConstructor
public class ClientRestApi {

    private final ClientService clientService;
    private final AccountService accountService;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.POST)
    public ClientDto create(@RequestBody ClientCreateRequestDto clientCreateRequest) {
        return dto(clientService.create(clientCreateRequest.getUsername()));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ClientDto one(@PathVariable("id") UUID id) {
        return dto(clientService.get(id));
    }

    @RequestMapping(value = "{id}/accounts", method = RequestMethod.GET)
    public List<AccountDto> accounts(@PathVariable("id") UUID id) {
        return accountService.listByClient(id).stream().map(AccountDto::dtoShort).toList();
    }

}
