package com.paysafe.buckpal.account.adapter.out.persistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.paysafe.buckpal.account.domain.Account;
import com.paysafe.buckpal.account.domain.Activity;
import com.paysafe.buckpal.account.domain.ActivityLedger;
import com.paysafe.buckpal.account.domain.Money;
import com.paysafe.buckpal.account.domain.AccountId;
import com.paysafe.buckpal.account.domain.ActivityId;

/**
 * See https://mapstruct.org for better performance and error handling.
 *
 * Practical example could be found here:
 *     https://reflectoring.io/java-mapping-with-mapstruct/
 */
@Component
class AccountMapper {

    // DB -> domain communication
    Account mapToDomainEntity(AccountJpaEntity account, List<ActivityJpaEntity> activities,
        Long withdrawalBalance, Long depositBalance) {

        final Money baselineBalance =
            Money.subtract(Money.of(depositBalance), Money.of(withdrawalBalance));

        return Account.withId(new AccountId(account.getId()), baselineBalance, mapToActivityWindow(activities));
    }

    ActivityLedger mapToActivityWindow(List<ActivityJpaEntity> activities) {
        final List<Activity> mappedActivities = new ArrayList<>();

        for (ActivityJpaEntity activity : activities) {
            mappedActivities.add(new Activity(
                new ActivityId(activity.getId()),
                new AccountId(activity.getOwnerAccountId()),
                new AccountId(activity.getSourceAccountId()),
                new AccountId(activity.getTargetAccountId()),
                activity.getTimestamp(),
                Money.of(activity.getAmount())));
        }

        return new ActivityLedger(mappedActivities);
    }

    // domain -> DB communication
    ActivityJpaEntity mapToJpaEntity(Activity activity) {
        return new ActivityJpaEntity(
            activity.getId() == null ? null : activity.getId().getValue(),
            activity.getTimestamp(),
            activity.getOwnerAccountId().getValue(),
            activity.getSourceAccountId().getValue(),
            activity.getTargetAccountId().getValue(),
            activity.getMoney().getAmount().longValue());
    }

}
