package mm.wallet.infrastructure.restapi.v1;

import static java.util.Collections.emptyList;
import static mm.wallet.infrastructure.restapi.v1.AccountRestApiTest.mockAccount;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import mm.wallet.domain.account.AccountService;
import mm.wallet.domain.account.transaction.TransactionHistoryService;
import mm.wallet.domain.client.Client;
import mm.wallet.domain.client.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;


@AutoConfigureWebTestClient
@WebFluxTest(ClientRestApi.class)
class ClientRestApiTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AccountService accountService;
    @MockBean
    private ClientService clientService;

    @MockBean
    private TransactionHistoryService transactionService;

    @Test
    void whenThreeClientAccountsExist_ResponseContainsAccountArrayJson() {
        // given
        var clientId = UUID.fromString("4ed98c57-4fe4-497e-bbc5-10a059c558b9");
        var account1 = mockAccount(UUID.fromString("a018fb55-ce19-4ae0-b271-b63c37e6699c"), clientId, Currency.getInstance("EUR"));
        var account2 = mockAccount(UUID.fromString("ce2e152a-77ad-4e98-9ee2-4fbd09c0b24e"), clientId, Currency.getInstance("GBP"));
        var account3 = mockAccount(UUID.fromString("c89271ce-0c6e-4ecb-abed-f3855b1084d6"), clientId, Currency.getInstance("USD"));
        when(accountService.listByClient(eq(clientId))).thenReturn(List.of(account1, account2, account3));

        // when
        this.webTestClient.get().uri("/clients/4ed98c57-4fe4-497e-bbc5-10a059c558b9/accounts")
                .exchange()
                // then
                .expectStatus().is2xxSuccessful()
                .expectBody().json("""
                        [
                            {
                                "id": "a018fb55-ce19-4ae0-b271-b63c37e6699c",
                                "currency": "EUR",
                                "clientId": "4ed98c57-4fe4-497e-bbc5-10a059c558b9"
                            },
                            {
                                "id": "ce2e152a-77ad-4e98-9ee2-4fbd09c0b24e",
                                "currency": "GBP",
                                "clientId": "4ed98c57-4fe4-497e-bbc5-10a059c558b9"
                            },
                            {
                                "id": "c89271ce-0c6e-4ecb-abed-f3855b1084d6",
                                "currency": "USD",
                                "clientId": "4ed98c57-4fe4-497e-bbc5-10a059c558b9"
                            }
                        ]
                        """, false);
    }

    @Test
    void whenZeroClientAccountsExist_ResponseContainsEmptyArrayJson() {
        // given
        var clientId = UUID.fromString("4ed98c57-4fe4-497e-bbc5-10a059c558b9");
        when(accountService.listByClient(eq(clientId))).thenReturn(emptyList());

        // when
        this.webTestClient.get().uri("/clients/4ed98c57-4fe4-497e-bbc5-10a059c558b9/accounts")
                .exchange()
                // then
                .expectStatus().is2xxSuccessful()
                .expectBody().json("[]");
    }

    @Test
    void whenClientIsCreated_ResponseContainsClientJson() {
        // given
        var clientId = UUID.fromString("4ed98c57-4fe4-497e-bbc5-10a059c558b9");
        var fullName = "John Smith";
        var client = mockClient(clientId, fullName);
        when(clientService.create(eq(fullName))).thenReturn(client);

        // when
        this.webTestClient.post().uri("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "fullName": "John Smith"
                        }
                        """)
                .exchange()
                // then
                .expectStatus().is2xxSuccessful()
                .expectBody().json("""
                        {
                            "id": "4ed98c57-4fe4-497e-bbc5-10a059c558b9",
                            "fullName": "John Smith"
                        }
                        """, false);
    }

    @Test
    void whenClientReturned_ResponseContainsClientJson() {
        // given
        var clientId = UUID.fromString("4ed98c57-4fe4-497e-bbc5-10a059c558b9");
        var fullName = "John Smith";
        var client = mockClient(clientId, fullName);
        when(clientService.find(eq(clientId))).thenReturn(Optional.of(client));

        // when
        this.webTestClient.get().uri("/clients/4ed98c57-4fe4-497e-bbc5-10a059c558b9")
                .exchange()
                // then
                .expectStatus().is2xxSuccessful()
                .expectBody().json("""
                        {
                            "id": "4ed98c57-4fe4-497e-bbc5-10a059c558b9",
                            "fullName": "John Smith"
                        }
                        """, false);
    }

    @Test
    void whenClientDoesNotExist_ResponseContains404Response() {
        // when
        this.webTestClient.get().uri("/clients/4ed98c57-4fe4-497e-bbc5-10a059c558b9")
                .exchange()
                // then
                .expectStatus().isNotFound();
    }

    private Client mockClient(UUID clientId, String fullName) {
        var client = mock(Client.class);
        when(client.id()).thenReturn(clientId);
        when(client.fullName()).thenReturn(fullName);
        return client;
    }
}