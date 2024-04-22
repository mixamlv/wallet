package mm.wallet.domain.client;


import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import mm.wallet.domain.account.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Client {
    private UUID id;
    private String username;
    private List<Account> accounts = new ArrayList<>();

    public Client(String username) {
        this.id = UUID.randomUUID();
        this.username = username;
    }

    void add(Account account) {
        account.clientId(this.id);
        accounts.add(account);
    }


}
