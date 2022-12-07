package com.paysafe.buckpal.account.application.port.out;

import java.time.LocalDateTime;

import com.paysafe.buckpal.account.domain.Account;
import com.paysafe.buckpal.account.domain.AccountId;

public interface LoadAccountPort {

    Account loadAccount(AccountId accountId, LocalDateTime baselineDate);

}
