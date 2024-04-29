package mm.wallet.infrastructure.restapi.v1;

import static mm.wallet.infrastructure.restapi.v1.dto.ClientDto.dto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import mm.wallet.domain.account.AccountService;
import mm.wallet.domain.client.ClientService;
import mm.wallet.infrastructure.restapi.v1.dto.AccountDto;
import mm.wallet.infrastructure.restapi.v1.dto.ClientCreateRequestDto;
import mm.wallet.infrastructure.restapi.v1.dto.ClientDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("clients")
@RequiredArgsConstructor
public class ClientRestApi {

    private final ClientService clientService;
    private final AccountService accountService;

    @RequestMapping(value = {"/", ""}, method = RequestMethod.POST)
    public ClientDto create(@RequestBody ClientCreateRequestDto clientCreateRequest) {
        return dto(clientService.create(clientCreateRequest.getFullName()));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<ClientDto> one(@PathVariable("id") UUID id) {
        return ResponseEntity.of(clientService.find(id).map(ClientDto::dto));
    }

    @RequestMapping(value = "{id}/accounts", method = RequestMethod.GET)
    public List<AccountDto> accounts(@PathVariable("id") UUID id) {
        return accountService.listByClient(id).stream().map(AccountDto::dtoShort).toList();
    }

}
