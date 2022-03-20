package co.tala.bankaccountmanagement.configs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
@Setter
@Getter
@NoArgsConstructor
public class ConfigProperties {

    @Value("${deposit-transaction-limit}")
    private int depositTransactionLimit;

    @Value("${withdraw-transactions-limit}")
    private int withdrawTransactionsLimit;

    @Value("${max-daily-depoits-in-a-day}")
    private int maxDailyDeposit;

    @Value("${max-daily-withdrawal-in-a-day}")
    private int maxDailyWithdrawals;

}
