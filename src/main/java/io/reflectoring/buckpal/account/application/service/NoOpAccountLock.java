package io.reflectoring.buckpal.account.application.service;

import org.springframework.stereotype.Component;

import io.reflectoring.buckpal.account.application.port.out.AccountLock;
import io.reflectoring.buckpal.account.domain.AccountId;

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
