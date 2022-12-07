package com.paysafe.buckpal.account.application.service;

import com.paysafe.buckpal.account.domain.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Particular use case specific configuration properties for money transfer.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTransferProperties {

    private Money maximumTransferThreshold = Money.of(1_000_000L);

}
