package mm.wallet.domain.client;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Client implements Serializable {
    @Id
    private UUID id = UUID.randomUUID();
    private String fullName;

    public Client(String fullName) {
        this.fullName = fullName;
    }

    public UUID id() {
        return id;
    }

    public String fullName() {
        return fullName;
    }
}
