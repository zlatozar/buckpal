package io.reflectoring.buckpal.account.application.port.in;

import io.reflectoring.buckpal.account.domain.AccountId;
import io.reflectoring.buckpal.account.domain.Money;

public interface GetAccountBalanceQuery {

    Money getAccountBalance(AccountId accountId);

}
