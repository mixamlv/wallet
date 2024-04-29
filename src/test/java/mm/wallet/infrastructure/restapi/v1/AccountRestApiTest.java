package mm.wallet.infrastructure.restapi.v1;

import static java.util.Collections.emptyList;
import static mm.wallet.domain.account.transaction.TransactionType.DEPOSIT;
import static mm.wallet.domain.account.transaction.TransactionType.WITHDRAWAL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import mm.wallet.domain.account.Account;
import mm.wallet.domain.account.AccountService;
import mm.wallet.domain.account.Transaction;
import mm.wallet.domain.account.transaction.TransactionHistoryService;
import mm.wallet.domain.account.transaction.TransactionType;
import mm.wallet.domain.account.transfer.AccountTransfer;
import mm.wallet.domain.account.transfer.AccountTransferService;
import mm.wallet.domain.client.Client;
import mm.wallet.domain.core.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;


@AutoConfigureWebTestClient
@WebFluxTest(AccountRestApi.class)
class AccountRestApiTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountTransferService accountTransferService;

    @MockBean
    private TransactionHistoryService transactionService;

    private static AccountTransfer mockTransfer(UUID id, Money money) {
        var accountTransfer = mock(AccountTransfer.class);
        when(accountTransfer.id()).thenReturn(id);
        when(accountTransfer.actualMoney()).thenReturn(money);
        when(accountTransfer.originalMoney()).thenReturn(money);
        when(accountTransfer.createdAt()).thenReturn(LocalDateTime.now());
        return accountTransfer;
    }

    public static Account mockAccount(UUID accountId, UUID clientId, Currency currency) {
        var account = mock(Account.class);
        when(account.id()).thenReturn(accountId);
        when(account.currency()).thenReturn(currency);
        var client = mock(Client.class);
        when(client.id()).thenReturn(clientId);
        when(account.client()).thenReturn(Optional.of(client));
        when(account.balance()).thenReturn(BigDecimal.ZERO);
        return account;
    }

    public static Transaction mockTx(Long id, TransactionType type, BigDecimal amount) {
        var tx = mock(Transaction.class);
        when(tx.id()).thenReturn(id);
        when(tx.type()).thenReturn(type);
        when(tx.amount()).thenReturn(amount);
        return tx;
    }

    @Test
    void whenEurAccountIsCreated_ResponseContainsAccountJson() {
        // given
        var accountId = UUID.fromString("a018fb55-ce19-4ae0-b271-b63c37e6699c");
        var clientId = UUID.fromString("4ed98c57-4fe4-497e-bbc5-10a059c558b9");
        var currency = Currency.getInstance("EUR");
        var account = mockAccount(accountId, clientId, currency);
        when(accountService.create(eq(currency), eq(clientId))).thenReturn(account);

        // when
        this.webTestClient.post().uri("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "currency": "EUR",
                            "clientId": "4ed98c57-4fe4-497e-bbc5-10a059c558b9"
                        }
                        """)
                .exchange()
                // then
                .expectStatus().is2xxSuccessful()
                .expectBody().json("""
                        {
                            "id": "a018fb55-ce19-4ae0-b271-b63c37e6699c",
                            "currency": "EUR",
                            "clientId": "4ed98c57-4fe4-497e-bbc5-10a059c558b9"
                        }
                        """, false);
    }

    @Test
    void whenAccountIsNotCreatedDueRuntimeException_ResponseContainsErrorJson() {
        // given
        when(accountService.create(any(), any())).thenThrow(RuntimeException.class);

        // when
        this.webTestClient.post().uri("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "currency": "EUR",
                            "clientId": "4ed98c57-4fe4-497e-bbc5-10a059c558b9"
                        }
                        """)
                .exchange()
                // then
                .expectStatus().is5xxServerError()
                .expectBody().json("""
                        {
                            "path": "/accounts",
                            "status": 500,
                            "error": "Internal Server Error"
                        }
                        """, false);
    }

    @Test
    void whenZeroAccountTransactionsExistAndNoLimitAndOffsetProvided_ResponseContainsEmptyListJsonAndDefaultValuesAreUsed() {
        // given
        var accountId = UUID.fromString("4ed98c57-4fe4-497e-bbc5-10a059c558b9");
        when(transactionService.historyByAccount(eq(accountId), anyLong(), anyLong())).thenReturn(emptyList());

        // when
        this.webTestClient.get().uri("/accounts/%s/transactions".formatted(accountId))
                .exchange()
                // then
                .expectStatus().is2xxSuccessful()
                .expectBody().json("[]");
        verify(transactionService, times(1))
                .historyByAccount(eq(accountId), eq(10L), eq(0L));
    }

    @Test
    void whenLimitIsZero_4xxResponse() {
        // given
        var accountId = UUID.fromString("4ed98c57-4fe4-497e-bbc5-10a059c558b9");
        // when
        this.webTestClient.get().uri("/accounts/%s/transactions?limit=0".formatted(accountId))
                .exchange()
                // then
                .expectStatus().is4xxClientError()
                .expectBody(String.class)
                .isEqualTo("historyByAccount.limit: must be greater than or equal to 1");
    }

    @Test
    void whenOffsetIsNegative_4xxResponse() {
        // when
        this.webTestClient.get().uri("/accounts/4ed98c57-4fe4-497e-bbc5-10a059c558b9/transactions?offset=-1")
                .exchange()
                // then
                .expectStatus().is4xxClientError()
                .expectBody(String.class)
                .isEqualTo("historyByAccount.offset: must be greater than or equal to 0");
    }

    @Test
    void whenZeroAccountTransactionsExistLimitAndOffsetProvided_ResponseContainsEmptyArrayJson() {
        // given
        var accountId = UUID.fromString("4ed98c57-4fe4-497e-bbc5-10a059c558b9");
        when(transactionService.historyByAccount(eq(accountId), anyLong(), anyLong())).thenReturn(emptyList());

        // when
        this.webTestClient.get().uri("/accounts/%s/transactions?limit=21&offset=12".formatted(accountId))
                .exchange()
                // then
                .expectStatus().is2xxSuccessful()
                .expectBody().json("[]");
        verify(transactionService, times(1))
                .historyByAccount(eq(accountId), eq(21L), eq(12L));
    }

    @Test
    void whenThreeAccountTransactionsExist_ResponseContainsArrayJson() {
        // given
        var accountId = UUID.fromString("4ed98c57-4fe4-497e-bbc5-10a059c558b9");
        var tx1 = mockTx(1L, DEPOSIT, new BigDecimal("24.22"));
        var tx2 = mockTx(2L, WITHDRAWAL, new BigDecimal("0.3"));
        var tx3 = mockTx(3L, DEPOSIT, new BigDecimal("11.00"));
        when(transactionService.historyByAccount(eq(accountId), anyLong(), anyLong()))
                .thenReturn(List.of(tx1, tx2, tx3));

        // when
        this.webTestClient.get().uri("/accounts/%s/transactions".formatted(accountId))
                .exchange()
                // then
                .expectStatus().is2xxSuccessful()
                .expectBody().json("""
                        [
                           {
                              "id": 1,
                              "type": "DEPOSIT",
                              "amount": 24.22
                           },
                           {
                              "id": 2,
                              "type": "WITHDRAWAL",
                              "amount": 0.3
                           },
                           {
                              "id": 3,
                              "type": "DEPOSIT",
                              "amount": 11.00
                           }
                        ]
                        """);
        verify(transactionService, times(1))
                .historyByAccount(eq(accountId), eq(10L), eq(0L));
    }

    @Test
    void whenDepositTransaction_ResponseContainsTxJson() {
        // given
        var accountId = UUID.fromString("4ed98c57-4fe4-497e-bbc5-10a059c558b9");
        var tx = mockTx(1L, DEPOSIT, new BigDecimal("11.22"));
        when(accountService.deposit(eq(accountId), eq(Money.money("11.22", "EUR"))))
                .thenReturn(tx);

        // when
        this.webTestClient.post().uri("/accounts/%s/transactions".formatted(accountId))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "money": {
                                "amount": "11.22",
                                "currency": "EUR"
                            },
                            "type": "DEPOSIT"
                        }
                        """)
                .exchange()
                // then
                .expectStatus().is2xxSuccessful()
                .expectBody().json("""
                        {
                           "id": 1,
                           "type": "DEPOSIT",
                           "amount": 11.22
                        }
                        """);
        verify(accountService, times(1))
                .deposit(eq(accountId), eq(Money.money("11.22", "EUR")));

    }

    @Test
    void whenWithdrawalTransaction_ResponseContainsTxJson() {
        // given
        var accountId = UUID.fromString("4ed98c57-4fe4-497e-bbc5-10a059c558b9");
        var tx = mockTx(1L, WITHDRAWAL, new BigDecimal("0.33"));
        when(accountService.withdraw(eq(accountId), eq(Money.money("0.33", "EUR"))))
                .thenReturn(tx);

        // when
        this.webTestClient.post().uri("/accounts/%s/transactions".formatted(accountId))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "money": {
                                "amount": "0.33",
                                "currency": "EUR"
                            },
                            "type": "WITHDRAWAL"
                        }
                        """)
                .exchange()
                // then
                .expectStatus().is2xxSuccessful()
                .expectBody().json("""
                        {
                           "id": 1,
                           "type": "WITHDRAWAL",
                           "amount": 0.33
                        }
                        """);
        verify(accountService, times(1))
                .withdraw(eq(accountId), eq(Money.money("0.33", "EUR")));

    }

    @Test
    void whenAccountTransfer_ResponseContainsTransferJson() {
        // given
        var sourceAccountId = UUID.fromString("4ed98c57-4fe4-497e-bbc5-10a059c558b9");
        var targetAccountId = UUID.fromString("a018fb55-ce19-4ae0-b271-b63c37e6699c");
        var transferId = UUID.fromString("a018fb55-ce19-4ae0-b271-b63c37e6699c");
        var transfer = mockTransfer(transferId, Money.money("3.22", "EUR"));
        when(accountTransferService.create(eq(sourceAccountId), eq(targetAccountId), eq(Money.money("3.22", "EUR"))))
                .thenReturn(transfer);

        // when
        this.webTestClient.post().uri("/accounts/%s/transfer".formatted(sourceAccountId))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "targetAccountId": "a018fb55-ce19-4ae0-b271-b63c37e6699c",
                            "money": {
                                "amount": "3.22",
                                "currency": "EUR"
                            }
                        }
                        """)
                .exchange()
                // then
                .expectStatus().is2xxSuccessful()
                .expectBody().json("""
                        {
                           "id": "a018fb55-ce19-4ae0-b271-b63c37e6699c",
                            "actualMoney": {
                                "amount": 3.22,
                                "currency": "EUR"
                            },
                            "originalMoney": {
                                "amount": 3.22,
                                "currency": "EUR"
                            }
                        }
                        """, false);
        verify(accountTransferService, times(1))
                .create(eq(sourceAccountId), eq(targetAccountId), eq(Money.money("3.22", "EUR")));

    }
}