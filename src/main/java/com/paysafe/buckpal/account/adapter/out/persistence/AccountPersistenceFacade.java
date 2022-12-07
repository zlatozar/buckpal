package com.paysafe.buckpal.account.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import com.paysafe.buckpal.account.application.port.out.LoadAccountPort;
import com.paysafe.buckpal.account.application.port.out.UpdateAccountStatePort;
import com.paysafe.buckpal.account.domain.Account;
import com.paysafe.buckpal.account.domain.AccountId;
import com.paysafe.buckpal.account.domain.Activity;
import com.paysafe.buckpal.common.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

/**
 * Persistence facade that converts domain output to database input action.
 */
@PersistenceAdapter
@RequiredArgsConstructor
class AccountPersistenceFacade implements LoadAccountPort, UpdateAccountStatePort {

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
