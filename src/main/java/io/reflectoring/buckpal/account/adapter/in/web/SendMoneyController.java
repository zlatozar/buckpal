package io.reflectoring.buckpal.account.adapter.in.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.reflectoring.buckpal.account.application.port.in.SendMoneyDTO;
import io.reflectoring.buckpal.account.application.port.in.SendMoneyUseCase;
import io.reflectoring.buckpal.account.domain.AccountId;
import io.reflectoring.buckpal.account.domain.Money;
import io.reflectoring.buckpal.common.WebAdapter;
import lombok.RequiredArgsConstructor;

/**
 * REST adapter entry point.
 * Contract should be specified by OpenAPI. More information: https://www.openapis.org
 */
@WebAdapter
@RestController
@RequiredArgsConstructor
class SendMoneyController {

    // Interface that represents 'send money' command in domain logic.
    private final SendMoneyUseCase sendMoneyUseCase;

    @PostMapping(path = "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
    void sendMoney(@PathVariable("sourceAccountId") Long sourceAccountId,
        @PathVariable("targetAccountId") Long targetAccountId,
        @PathVariable("amount") Long amount) {

        // Collect all needed data for the particular command.
        SendMoneyDTO command =
            new SendMoneyDTO(new AccountId(sourceAccountId), new AccountId(targetAccountId), Money.of(amount));

        sendMoneyUseCase.sendMoney(command);
    }

}
