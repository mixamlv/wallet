package mm.wallet.infrastructure.restapi.v1.dto;

import lombok.Data;
import mm.wallet.domain.client.Client;

import java.util.UUID;

@Data
public class ClientDto {

    private UUID id;
    private String username;

    public static ClientDto dto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.id());
        dto.setUsername(client.username());
        return dto;
    }
}
