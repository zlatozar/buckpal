package io.reflectoring.buckpal.account.application.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import io.reflectoring.buckpal.account.application.port.in.SendMoneyCommand;
import io.reflectoring.buckpal.account.application.port.in.SendMoneyUseCase;
import io.reflectoring.buckpal.account.application.port.out.AccountLock;
import io.reflectoring.buckpal.account.application.port.out.LoadAccountPort;
import io.reflectoring.buckpal.account.application.port.out.UpdateAccountStatePort;
import io.reflectoring.buckpal.account.domain.Account;
import io.reflectoring.buckpal.account.domain.Account.AccountId;
import io.reflectoring.buckpal.common.UseCase;
import lombok.RequiredArgsConstructor;

/**
 * Use case implementation.
 */
@UseCase
@Transactional
@RequiredArgsConstructor
public class SendMoneyService implements SendMoneyUseCase {

    // Order matters
    private final LoadAccountPort loadAccountPort;
    private final AccountLock accountLock;
    private final UpdateAccountStatePort updateAccountStatePort;
    private final MoneyTransferProperties moneyTransferProperties;

    @Override
    public boolean sendMoney(SendMoneyCommand command) {

        checkThreshold(command);

        LocalDateTime baselineDate = LocalDateTime.now().minusDays(10);

        Account sourceAccount = loadAccountPort.loadAccount(command.getSourceAccountId(), baselineDate);

        Account targetAccount = loadAccountPort.loadAccount(command.getTargetAccountId(), baselineDate);

        AccountId sourceAccountId = sourceAccount.getId();
        AccountId targetAccountId = targetAccount.getId();

        accountLock.lockAccount(sourceAccountId);

        // May withdraw or not?
        if (!sourceAccount.withdraw(command.getMoney(), targetAccountId)) {
            accountLock.releaseAccount(sourceAccountId);

            return false;
        }

        accountLock.lockAccount(targetAccountId);

        if (!targetAccount.deposit(command.getMoney(), sourceAccountId)) {
            accountLock.releaseAccount(sourceAccountId);
            accountLock.releaseAccount(targetAccountId);

            return false;
        }

        updateAccountStatePort.updateActivities(sourceAccount);
        updateAccountStatePort.updateActivities(targetAccount);

        accountLock.releaseAccount(sourceAccountId);
        accountLock.releaseAccount(targetAccountId);

        return true;
    }

    // Helper methods

    private void checkThreshold(SendMoneyCommand command) {
        if (command.getMoney().isGreaterThan(moneyTransferProperties.getMaximumTransferThreshold())) {
            throw new ThresholdExceededException(moneyTransferProperties.getMaximumTransferThreshold(), command.getMoney());
        }
    }

}




