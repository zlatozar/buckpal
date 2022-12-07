package com.paysafe.buckpal.common;

import com.paysafe.buckpal.account.domain.Account;
import com.paysafe.buckpal.account.domain.ActivityLedger;
import com.paysafe.buckpal.account.domain.Money;
import com.paysafe.buckpal.account.domain.AccountId;

public class AccountTestData {

    public static AccountBuilder defaultAccount() {
        return new AccountBuilder()
            .withAccountId(new AccountId(42L))
            .withBaselineBalance(Money.of(999L))
            .withActivityWindow(new ActivityLedger(
                ActivityTestData.defaultActivity().build(),
                ActivityTestData.defaultActivity().build()));
    }

    // Inner class

    public static class AccountBuilder {

        private AccountId accountId;
        private Money baselineBalance;
        private ActivityLedger activityLedger;

        public AccountBuilder withAccountId(AccountId accountId) {
            this.accountId = accountId;
            return this;
        }

        public AccountBuilder withBaselineBalance(Money baselineBalance) {
            this.baselineBalance = baselineBalance;
            return this;
        }

        public AccountBuilder withActivityWindow(ActivityLedger activityLedger) {
            this.activityLedger = activityLedger;
            return this;
        }

        public Account build() {
            return Account.withId(this.accountId, this.baselineBalance, this.activityLedger);
        }

    }

}
