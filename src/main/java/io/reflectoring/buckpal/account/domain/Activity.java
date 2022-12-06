package io.reflectoring.buckpal.account.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Entity object that represents a money transfer activity between {@link Account}s.
 */
@Value
@RequiredArgsConstructor
public class Activity {

    @Getter
    private ActivityId id;

    /**
     * The account that owns this activity.
     */
    @Getter
    @NonNull
    private final AccountId ownerAccountId;

    /**
     * The debited account.
     */
    @Getter
    @NonNull
    private final AccountId sourceAccountId;

    /**
     * The credited account.
     */
    @Getter
    @NonNull
    private final AccountId targetAccountId;

    /**
     * The timestamp of the activity.
     */
    @Getter
    @NonNull
    private final LocalDateTime timestamp;

    /**
     * The money that was transferred between the accounts.
     */
    @Getter
    @NonNull
    private final Money money;

    public Activity(@NonNull AccountId ownerAccountId,
        @NonNull AccountId sourceAccountId,
        @NonNull AccountId targetAccountId,
        @NonNull LocalDateTime timestamp,
        @NonNull Money money) {

        this.id = null;

        this.ownerAccountId = ownerAccountId;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.timestamp = timestamp;
        this.money = money;
    }

}
