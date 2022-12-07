package io.reflectoring.buckpal.account.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static io.reflectoring.buckpal.common.AccountTestData.defaultAccount;
import static io.reflectoring.buckpal.common.ActivityTestData.defaultActivity;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import io.reflectoring.buckpal.account.domain.Account;
import io.reflectoring.buckpal.account.domain.AccountId;
import io.reflectoring.buckpal.account.domain.ActivityLedger;
import io.reflectoring.buckpal.account.domain.Money;

@DataJpaTest
@Import({ AccountPersistence.class, AccountMapper.class })
class AccountPersistenceTest {

    @Autowired
    private AccountPersistence adapterUnderTest;

    @Autowired
    private ActivityQueryRepository activityQueryRepository;

    @Test
    @Sql("AccountPersistenceAdapterTest.sql")
    void loadsAccount() {
        Account account = adapterUnderTest.loadAccount(new AccountId(1L),
            LocalDateTime.of(2018, 8, 10, 0, 0));

        assertThat(account.getActivityLedger().getActivities()).hasSize(2);
        assertThat(account.calculateBalance()).isEqualTo(Money.of(500));
    }

    @Test
    void updatesActivities() {
        Account account = defaultAccount()
            .withBaselineBalance(Money.of(555L))
            .withActivityWindow(new ActivityLedger(defaultActivity()
                .withId(null).withMoney(Money.of(1L)).build()))
            .build();

        adapterUnderTest.updateActivities(account);

        assertThat(activityQueryRepository.count()).isEqualTo(1);

        ActivityJpaEntity savedActivity = activityQueryRepository.findAll().get(0);
        assertThat(savedActivity.getAmount()).isEqualTo(1L);
    }

}