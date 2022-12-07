package io.reflectoring.buckpal.account.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.NonNull;
import lombok.Value;

/**
 * A ledger of account activities.
 */
@Value
public class ActivityLedger {

    /**
     * The list of account activities within this time window.
     */
    private final List<Activity> activities;

    public ActivityLedger(@NonNull List<Activity> activities) {
        this.activities = activities;
    }

    public ActivityLedger(@NonNull Activity... activities) {
        this.activities = new ArrayList<>(Arrays.asList(activities));
    }

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(this.activities);
    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
    }

    /**
     * The timestamp of the first activity within this time window.
     */
    public LocalDateTime getStartTimestamp() {
        return activities.stream()
            .min(Comparator.comparing(Activity::getTimestamp))
            .orElseThrow(IllegalStateException::new)
            .getTimestamp();
    }

    /**
     * The timestamp of the last activity within this time window.
     */
    public LocalDateTime getEndTimestamp() {
        return activities.stream()
            .max(Comparator.comparing(Activity::getTimestamp))
            .orElseThrow(IllegalStateException::new)
            .getTimestamp();
    }

    /**
     * Calculates the balance by summing up the values of all activities
     * within this time window.
     */
    public Money calculateBalance(AccountId accountId) {
        final Money depositBalance = activities.stream()
            .filter(a -> a.getTargetAccountId().equals(accountId))
            .map(Activity::getMoney)
            .reduce(Money.ZERO, Money::add);

        final Money withdrawalBalance = activities.stream()
            .filter(a -> a.getSourceAccountId().equals(accountId))
            .map(Activity::getMoney)
            .reduce(Money.ZERO, Money::add);

        return Money.add(depositBalance, withdrawalBalance.negate());
    }
}
