package mm.wallet;

import static mm.wallet.model.AddTransactionRequestDto.TypeEnum.DEPOSIT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigDecimal;
import mm.wallet.model.AddTransactionRequestDto;
import mm.wallet.model.ClientCreateRequestDto;
import mm.wallet.model.CreateAccountRequestDto;
import mm.wallet.model.CreateAccountTransferRequestDto;
import mm.wallet.model.MoneyDto;
import mm.wallet.model.TransactionDto;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.test.StepVerifier;

public class AccountTransferIT extends BaseIntegrationTest {

    private static MoneyDto m(String amount, String currencyCode) {
        return new MoneyDto().amount(new BigDecimal(amount)).currency(currencyCode);
    }

    @Test
    void whenAccountTransferBetweenSameClientAccountsWithSameCurrency() {
        // given
        var client = clientApi.create(new ClientCreateRequestDto().fullName("Janis Berziņš")).block();
        var account1 = accountApi.create2(new CreateAccountRequestDto().clientId(client.getId())
                .currency("EUR")).block();
        var account2 = accountApi.create2(new CreateAccountRequestDto().clientId(client.getId())
                .currency("EUR")).block();
        accountApi.addTransaction(account1.getId(), new AddTransactionRequestDto()
                .money(m("10", "EUR"))
                .type(DEPOSIT)).block();
        // when
        accountApi.transfer(account1.getId(), new CreateAccountTransferRequestDto()
                .targetAccountId(account2.getId())
                .money(m("3.22", "EUR"))).block();

        // then
        StepVerifier.create(accountApi.get(account1.getId()))
                .assertNext(account -> {
                    assertEquals(new BigDecimal("6.78"), account.getBalance());
                })
                .verifyComplete();
        StepVerifier.create(accountApi.get(account2.getId()))
                .assertNext(account -> {
                    assertEquals(new BigDecimal("3.22"), account.getBalance());
                })
                .verifyComplete();

        // and when
        StepVerifier.create(accountApi.transfer(account1.getId(), new CreateAccountTransferRequestDto()
                        .targetAccountId(account2.getId())
                        .money(m("6.79", "EUR"))))
                .consumeErrorWith(exception -> {
                    WebClientResponseException.BadRequest badRequestException = (WebClientResponseException.BadRequest) exception;
                    assertEquals("Insufficient Account funds", badRequestException.getResponseBodyAsString());
                })
                .verify();
    }

    @Test
    void whenAccountTransferBetweenSameClientAccountWithExchange() {
        // given
        var client = clientApi.create(new ClientCreateRequestDto().fullName("Juris Lusis")).block();
        var account1 = accountApi.create2(new CreateAccountRequestDto().clientId(client.getId())
                .currency("EUR")).block();
        var account2 = accountApi.create2(new CreateAccountRequestDto().clientId(client.getId())
                .currency("USD")).block();
        accountApi.addTransaction(account1.getId(), new AddTransactionRequestDto()
                .money(m("10", "EUR"))
                .type(DEPOSIT)).block();
        // when
        accountApi.transfer(account1.getId(), new CreateAccountTransferRequestDto()
                .targetAccountId(account2.getId())
                .money(m("5", "USD"))).block();

        // then
        StepVerifier.create(accountApi.get(account1.getId()))
                .assertNext(account -> {
                    assertEquals(new BigDecimal("5.34"), account.getBalance());
                })
                .verifyComplete();
        StepVerifier.create(accountApi.get(account2.getId()))
                .assertNext(account -> {
                    assertEquals(new BigDecimal("5.00"), account.getBalance());
                })
                .verifyComplete();

        StepVerifier.create(accountApi.historyByAccount(account1.getId(), 10L, 0L))
                .assertNext(tx -> {
                    assertEquals(TransactionDto.TypeEnum.WITHDRAWAL, tx.getType());
                    assertEquals(new BigDecimal("4.66"), tx.getAmount());
                })
                .assertNext(tx -> {
                    assertEquals(TransactionDto.TypeEnum.DEPOSIT, tx.getType());
                    assertEquals(new BigDecimal("10.00"), tx.getAmount());
                }).verifyComplete();

    }

}
