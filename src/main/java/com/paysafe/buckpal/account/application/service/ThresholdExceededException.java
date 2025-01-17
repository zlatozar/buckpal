package com.paysafe.buckpal.account.application.service;

import com.paysafe.buckpal.account.domain.Money;

/**
 * Expected business exception that domain logic should know how to handle.
 */
public class ThresholdExceededException extends RuntimeException {

    public ThresholdExceededException(Money threshold, Money actual) {
        super(String.format("Maximum threshold for transferring money exceeded: tried to transfer %s but threshold is %s!",
            actual, threshold));
    }

}
