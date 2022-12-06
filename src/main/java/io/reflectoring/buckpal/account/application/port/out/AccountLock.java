package io.reflectoring.buckpal.account.application.port.out;

import io.reflectoring.buckpal.account.domain.AccountId;

public interface AccountLock {

    void lockAccount(AccountId accountId);

    void releaseAccount(AccountId accountId);

}
