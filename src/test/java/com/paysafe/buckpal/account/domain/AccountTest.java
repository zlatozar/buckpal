package com.paysafe.buckpal.account.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.paysafe.buckpal.common.AccountTestData;
import com.paysafe.buckpal.common.ActivityTestData;

class AccountTest {

    @Test
    void calculatesBalance() {
        AccountId accountId = new AccountId(1L);
        Account account = AccountTestData.defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555L))
            .withActivityWindow(new ActivityLedger(
                ActivityTestData.defaultActivity().withTargetAccount(accountId).withMoney(Money.of(999L)).build(),
                ActivityTestData.defaultActivity().withTargetAccount(accountId).withMoney(Money.of(1L)).build()))
            .build();

        Money balance = account.calculateBalance();

        assertThat(balance).isEqualTo(Money.of(1555L));
    }

    @Test
    void withdrawalSucceeds() {
        AccountId accountId = new AccountId(1L);
        Account account = AccountTestData.defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555L))
            .withActivityWindow(new ActivityLedger(
                ActivityTestData.defaultActivity().withTargetAccount(accountId).withMoney(Money.of(999L)).build(),
                ActivityTestData.defaultActivity().withTargetAccount(accountId).withMoney(Money.of(1L)).build()))
            .build();

        boolean success = account.withdraw(Money.of(555L), new AccountId(99L));

        assertThat(success).isTrue();
        assertThat(account.getActivityLedger().getActivities()).hasSize(3);
        assertThat(account.calculateBalance()).isEqualTo(Money.of(1000L));
    }

    @Test
    void withdrawalFailure() {
        AccountId accountId = new AccountId(1L);
        Account account = AccountTestData.defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555L))
            .withActivityWindow(new ActivityLedger(
                ActivityTestData.defaultActivity().withTargetAccount(accountId).withMoney(Money.of(999L)).build(),
                ActivityTestData.defaultActivity().withTargetAccount(accountId).withMoney(Money.of(1L)).build()))
            .build();

        boolean success = account.withdraw(Money.of(1556L), new AccountId(99L));

        assertThat(success).isFalse();
        assertThat(account.getActivityLedger().getActivities()).hasSize(2);
        assertThat(account.calculateBalance()).isEqualTo(Money.of(1555L));
    }

    @Test
    void depositSuccess() {
        AccountId accountId = new AccountId(1L);
        Account account = AccountTestData.defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555L))
            .withActivityWindow(new ActivityLedger(
                ActivityTestData.defaultActivity().withTargetAccount(accountId).withMoney(Money.of(999L)).build(),
                ActivityTestData.defaultActivity().withTargetAccount(accountId).withMoney(Money.of(1L)).build()))
            .build();

        boolean success = account.deposit(Money.of(445L), new AccountId(99L));

        assertThat(success).isTrue();
        assertThat(account.getActivityLedger().getActivities()).hasSize(3);
        assertThat(account.calculateBalance()).isEqualTo(Money.of(2000L));
    }

}