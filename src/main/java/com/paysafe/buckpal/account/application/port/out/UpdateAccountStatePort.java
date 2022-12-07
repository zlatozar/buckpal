package com.paysafe.buckpal.account.application.port.out;

import com.paysafe.buckpal.account.domain.Account;

public interface UpdateAccountStatePort {

    void updateActivities(Account account);

}
