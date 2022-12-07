package com.paysafe.buckpal.account.application.service;

import com.paysafe.buckpal.account.domain.AccountId;

/**
 * Hide implementations behind interfaces. This principle
 * is base for applying Design Patterns.
 */
interface AccountLock {

    void lockAccount(AccountId accountId);

    void releaseAccount(AccountId accountId);

}
