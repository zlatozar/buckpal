package com.paysafe.buckpal;

import static org.assertj.core.api.BDDAssertions.then;

import java.time.LocalDateTime;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import com.paysafe.buckpal.account.application.port.out.LoadAccountPort;
import com.paysafe.buckpal.account.domain.Account;
import com.paysafe.buckpal.account.domain.Money;
import com.paysafe.buckpal.account.domain.AccountId;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SendMoneySystemTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LoadAccountPort loadAccountPort;

    @Test
    @Sql("SendMoneySystemTest.sql")
    void sendMoney() {

        Money initialSourceBalance = sourceAccount().calculateBalance();
        Money initialTargetBalance = targetAccount().calculateBalance();

        ResponseEntity response = whenSendMoney(sourceAccountId(), targetAccountId(), transferredAmount());

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        BDDAssertions.then(sourceAccount().calculateBalance())
            .isEqualTo(initialSourceBalance.minus(transferredAmount()));

        BDDAssertions.then(targetAccount().calculateBalance())
            .isEqualTo(initialTargetBalance.plus(transferredAmount()));

    }

    // Helper methods

    private Account sourceAccount() {
        return loadAccount(sourceAccountId());
    }

    private Account targetAccount() {
        return loadAccount(targetAccountId());
    }

    private Account loadAccount(AccountId accountId) {
        return loadAccountPort.loadAccount(accountId, LocalDateTime.now());
    }

    private ResponseEntity whenSendMoney(AccountId sourceAccountId, AccountId targetAccountId, Money amount) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        return restTemplate.exchange(
            "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}",
            HttpMethod.POST,
            request,
            Object.class,
            sourceAccountId.getValue(),
            targetAccountId.getValue(),
            amount.getAmount());
    }

    private Money transferredAmount() {
        return Money.of(500L);
    }

    private Money balanceOf(AccountId accountId) {
        Account account = loadAccountPort.loadAccount(accountId, LocalDateTime.now());
        return account.calculateBalance();
    }

    private AccountId sourceAccountId() {
        return new AccountId(1L);
    }

    private AccountId targetAccountId() {
        return new AccountId(2L);
    }

}
