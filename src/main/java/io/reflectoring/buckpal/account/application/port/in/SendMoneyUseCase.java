package io.reflectoring.buckpal.account.application.port.in;

/**
 * Define commands that domain logic could fulfill.
 * This is the entry point of the use case.
 */
public interface SendMoneyUseCase {

    boolean sendMoney(SendMoneyDTO command);

}
