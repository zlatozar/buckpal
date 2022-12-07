package io.reflectoring.buckpal.account.application.service;

import io.reflectoring.buckpal.account.domain.AccountId;

/**
 * Hide implementations behind interfaces. This principle
 * is base for applying Design Patterns.
 */
interface AccountLock {

    void lockAccount(AccountId accountId);

    void releaseAccount(AccountId accountId);

}
