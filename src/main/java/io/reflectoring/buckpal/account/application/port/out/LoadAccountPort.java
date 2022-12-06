package io.reflectoring.buckpal.account.application.port.out;

import java.time.LocalDateTime;

import io.reflectoring.buckpal.account.domain.Account;
import io.reflectoring.buckpal.account.domain.AccountId;

public interface LoadAccountPort {

    Account loadAccount(AccountId accountId, LocalDateTime baselineDate);

}
