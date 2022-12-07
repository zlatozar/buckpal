package com.paysafe.buckpal.account.domain;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.paysafe.buckpal.common.ActivityTestData;

class ActivityLedgerTest {

    @Test
    void calculatesStartTimestamp() {
        ActivityLedger window = new ActivityLedger(
            ActivityTestData.defaultActivity().withTimestamp(startDate()).build(),
            ActivityTestData.defaultActivity().withTimestamp(inBetweenDate()).build(),
            ActivityTestData.defaultActivity().withTimestamp(endDate()).build());

        Assertions.assertThat(window.getStartTimestamp()).isEqualTo(startDate());
    }

    @Test
    void calculatesEndTimestamp() {
        ActivityLedger window = new ActivityLedger(
            ActivityTestData.defaultActivity().withTimestamp(startDate()).build(),
            ActivityTestData.defaultActivity().withTimestamp(inBetweenDate()).build(),
            ActivityTestData.defaultActivity().withTimestamp(endDate()).build());

        Assertions.assertThat(window.getEndTimestamp()).isEqualTo(endDate());
    }

    @Test
    void calculatesBalance() {

        AccountId account1 = new AccountId(1L);
        AccountId account2 = new AccountId(2L);

        ActivityLedger window = new ActivityLedger(
            ActivityTestData.defaultActivity()
                .withSourceAccount(account1)
                .withTargetAccount(account2)
                .withMoney(Money.of(999)).build(),
            ActivityTestData.defaultActivity()
                .withSourceAccount(account1)
                .withTargetAccount(account2)
                .withMoney(Money.of(1)).build(),
            ActivityTestData.defaultActivity()
                .withSourceAccount(account2)
                .withTargetAccount(account1)
                .withMoney(Money.of(500)).build());

        Assertions.assertThat(window.calculateBalance(account1)).isEqualTo(Money.of(-500));
        Assertions.assertThat(window.calculateBalance(account2)).isEqualTo(Money.of(500));
    }

    // Helper methods

    private LocalDateTime startDate() {
        return LocalDateTime.of(2019, 8, 3, 0, 0);
    }

    private LocalDateTime inBetweenDate() {
        return LocalDateTime.of(2019, 8, 4, 0, 0);
    }

    private LocalDateTime endDate() {
        return LocalDateTime.of(2019, 8, 5, 0, 0);
    }

}