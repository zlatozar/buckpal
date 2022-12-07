package io.reflectoring.buckpal.account.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import io.reflectoring.buckpal.account.application.port.out.LoadAccountPort;
import io.reflectoring.buckpal.account.application.port.out.UpdateAccountStatePort;
import io.reflectoring.buckpal.account.domain.Account;
import io.reflectoring.buckpal.account.domain.AccountId;
import io.reflectoring.buckpal.account.domain.Activity;
import io.reflectoring.buckpal.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

/**
 * Converts domain output to database input action.
 */
@PersistenceAdapter
@RequiredArgsConstructor
class AccountPersistence implements LoadAccountPort, UpdateAccountStatePort {

    private final AccountRepository accountRepository;
    private final ActivityQueryRepository activityQueryRepository;
    private final AccountMapper accountMapper;

    @Override
    public Account loadAccount(AccountId accountId, LocalDateTime baselineDate) {

        final AccountJpaEntity account = accountRepository
            .findById(accountId.getValue())
            .orElseThrow(EntityNotFoundException::new);

        final List<ActivityJpaEntity> activities =
            activityQueryRepository.findByOwnerSince(accountId.getValue(), baselineDate);

        final Long withdrawalBalance = orZero(activityQueryRepository
            .getWithdrawalBalanceUntil(accountId.getValue(), baselineDate));

        final Long depositBalance = orZero(activityQueryRepository
            .getDepositBalanceUntil(accountId.getValue(), baselineDate));

        return accountMapper.mapToDomainEntity(account, activities, withdrawalBalance, depositBalance);

    }

    @Override
    public void updateActivities(Account account) {
        for (Activity activity : account.getActivityLedger().getActivities()) {

            if (activity.getId() == null) {
                activityQueryRepository.save(accountMapper.mapToJpaEntity(activity));
            }
        }
    }

    // Helper methods

    private Long orZero(Long value) {
        return value == null ? 0L : value;
    }

}
