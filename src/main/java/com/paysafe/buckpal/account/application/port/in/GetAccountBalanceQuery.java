package com.paysafe.buckpal.account.application.port.in;

import com.paysafe.buckpal.account.domain.AccountId;
import com.paysafe.buckpal.account.domain.Money;

public interface GetAccountBalanceQuery {

    Money getAccountBalance(AccountId accountId);

}
