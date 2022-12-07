package io.reflectoring.buckpal.account.application.port.in;

import javax.validation.constraints.NotNull;

import io.reflectoring.buckpal.account.domain.AccountId;
import io.reflectoring.buckpal.account.domain.Money;
import io.reflectoring.buckpal.common.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

/**
 * Hub all data that is needed to execute the command defined
 * in the interface. It is a good practice to implement `toString`.
 *
 * @see SendMoneyUseCase#sendMoney(SendMoneyDTO)
 */
@Value
@EqualsAndHashCode(callSuper = false)
@ToString
public class SendMoneyDTO extends SelfValidating<SendMoneyDTO> {

    @NotNull
    private final AccountId sourceAccountId;

    @NotNull
    private final AccountId targetAccountId;

    @NotNull
    private final Money money;

    public SendMoneyDTO(AccountId sourceAccountId, AccountId targetAccountId, Money money) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.money = money;

        this.validateSelf();
    }
}
