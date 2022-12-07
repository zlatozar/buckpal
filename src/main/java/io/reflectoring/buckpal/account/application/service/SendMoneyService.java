package io.reflectoring.buckpal.account.application.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import io.reflectoring.buckpal.account.application.port.in.SendMoneyDTO;
import io.reflectoring.buckpal.account.application.port.in.SendMoneyUseCase;
import io.reflectoring.buckpal.account.application.port.out.LoadAccountPort;
import io.reflectoring.buckpal.account.application.port.out.UpdateAccountStatePort;
import io.reflectoring.buckpal.account.domain.Account;
import io.reflectoring.buckpal.account.domain.AccountId;
import io.reflectoring.buckpal.common.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 'Send money' sse case implementation.
 */
@UseCase
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SendMoneyService implements SendMoneyUseCase {

    // Order matters
    private final LoadAccountPort loadAccountPort;
    private final AccountLock accountLock;
    private final UpdateAccountStatePort updateAccountStatePort;
    private final MoneyTransferProperties moneyTransferProperties;

    @Override
    public boolean sendMoney(SendMoneyDTO command) {

        // Validate, but business should know how to handle it.
        checkThreshold(command);

        final LocalDateTime baselineDate = LocalDateTime.now().minusDays(10);

        final Account sourceAccount = loadAccountPort.loadAccount(command.getSourceAccountId(), baselineDate);
        final Account targetAccount = loadAccountPort.loadAccount(command.getTargetAccountId(), baselineDate);

        final AccountId sourceAccountId = sourceAccount.getId();
        final AccountId targetAccountId = targetAccount.getId();

        accountLock.lockAccount(sourceAccountId);

        // May withdraw or not?
        if (!sourceAccount.withdraw(command.getMoney(), targetAccountId)) {
            log.error("Sender '{}' doesn't have enough money. Transfer fails.", sourceAccountId);
            accountLock.releaseAccount(sourceAccountId);

            return false;
        }

        accountLock.lockAccount(targetAccountId);

        if (!targetAccount.deposit(command.getMoney(), sourceAccountId)) {
            accountLock.releaseAccount(sourceAccountId);
            accountLock.releaseAccount(targetAccountId);

            log.error("Can't write in financial ledger of user '{}'. Transfer fails", targetAccountId);
            return false;
        }

        updateAccountStatePort.updateActivities(sourceAccount);
        updateAccountStatePort.updateActivities(targetAccount);

        accountLock.releaseAccount(sourceAccountId);
        accountLock.releaseAccount(targetAccountId);

        log.info("Successful transfer {} -> {}", sourceAccount, targetAccount);
        return true;
    }

    // Helper methods

    private void checkThreshold(SendMoneyDTO command) {
        if (command.getMoney()
            .isGreaterThan(moneyTransferProperties.getMaximumTransferThreshold())) {

            throw new ThresholdExceededException(
                moneyTransferProperties.getMaximumTransferThreshold(), command.getMoney());
        }
    }

}




