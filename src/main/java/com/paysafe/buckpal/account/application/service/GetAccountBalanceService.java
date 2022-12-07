package com.paysafe.buckpal.account.application.service;

import java.time.LocalDateTime;

import com.paysafe.buckpal.account.application.port.in.GetAccountBalanceQuery;
import com.paysafe.buckpal.account.application.port.out.LoadAccountPort;
import com.paysafe.buckpal.account.domain.AccountId;
import com.paysafe.buckpal.account.domain.Money;
import lombok.RequiredArgsConstructor;

/**
 * In terms of DDD this is a Domain Service - do some work that is
 * common and part of many use cases.
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
