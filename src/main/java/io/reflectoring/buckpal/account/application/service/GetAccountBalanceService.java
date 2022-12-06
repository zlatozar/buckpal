package io.reflectoring.buckpal.account.application.service;

import java.time.LocalDateTime;

import io.reflectoring.buckpal.account.application.port.in.GetAccountBalanceQuery;
import io.reflectoring.buckpal.account.application.port.out.LoadAccountPort;
import io.reflectoring.buckpal.account.domain.AccountId;
import io.reflectoring.buckpal.account.domain.Money;
import lombok.RequiredArgsConstructor;

/**
 * In terms of DDD this is a Domain Service - do some work that is
 * command and part of many use cases.
 */
@RequiredArgsConstructor
class GetAccountBalanceService implements GetAccountBalanceQuery {

    private final LoadAccountPort loadAccountPort;

    @Override
    public Money getAccountBalance(AccountId accountId) {
        return loadAccountPort
            .loadAccount(accountId, LocalDateTime.now())
            .calculateBalance();
    }
}
