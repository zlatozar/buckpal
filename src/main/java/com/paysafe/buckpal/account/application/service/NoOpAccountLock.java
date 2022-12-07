package com.paysafe.buckpal.account.application.service;

import org.springframework.stereotype.Component;

import com.paysafe.buckpal.account.domain.AccountId;

@Component
class NoOpAccountLock implements AccountLock {

    @Override
    public void lockAccount(AccountId accountId) {
        // do nothing
    }

    @Override
    public void releaseAccount(AccountId accountId) {
        // do nothing
    }

}
