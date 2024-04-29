package mm.wallet.domain.client;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Client create(String fullName) {
        return clientRepository.save(new Client(fullName));
    }

    @Override
    public Optional<Client> find(UUID id) {
        return clientRepository.findById(id);
    }
}
