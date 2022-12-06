package io.reflectoring.buckpal.account.application.port.in;

/**
 * Define commands that domain logic could fulfill.
 */
public interface SendMoneyUseCase {

    boolean sendMoney(SendMoneyDTO command);

}
