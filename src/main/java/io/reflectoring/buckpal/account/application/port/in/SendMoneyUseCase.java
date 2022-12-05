package io.reflectoring.buckpal.account.application.port.in;

/**
 * Define possible use case operations/commands.
 */
public interface SendMoneyUseCase {

    boolean sendMoney(SendMoneyCommand command);

}
