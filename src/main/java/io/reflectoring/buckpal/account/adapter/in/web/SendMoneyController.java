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
import lombok.extern.slf4j.Slf4j;

/**
 * REST adapter entry point.
 * Contract with all it's restrictions should be specified by OpenAPI.
 *
 * More information: https://www.openapis.org
 */
@Slf4j
@WebAdapter
@RestController
@RequiredArgsConstructor
class SendMoneyController {

    // Interface that represents 'send money' command in domain logic.
    private final SendMoneyUseCase sendMoneyUseCase;

    // TIP: `/account/...` and `/accounts/...` are handled by `io.reflectoring.buckpal.account` module

    @PostMapping(path = "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
    void sendMoney(@PathVariable("sourceAccountId") Long sourceAccountId,
        @PathVariable("targetAccountId") Long targetAccountId,
        @PathVariable("amount") Long amount) {

        log.info("HTTP Request was received. '{}' will be transferred.", amount);

        // TIP: Parameter validation should be made by OpenAPI specification.

        // Collect all needed data for the particular command.
        final SendMoneyDTO dto = new SendMoneyDTO(new AccountId(sourceAccountId),
            new AccountId(targetAccountId), Money.of(amount));

        sendMoneyUseCase.sendMoney(dto);
    }
}
