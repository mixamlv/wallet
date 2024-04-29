package mm.wallet.infrastructure.restapi.v1.dto;

import java.util.UUID;
import lombok.Data;
import mm.wallet.domain.client.Client;

@Data
public class ClientDto {

    private UUID id;
    private String fullName;

    public static ClientDto dto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.id());
        dto.setFullName(client.fullName());
        return dto;
    }
}
