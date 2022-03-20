package co.tala.bankaccountmanagement.repositories;

import co.tala.bankaccountmanagement.entities.AccountEntity;
import co.tala.bankaccountmanagement.entities.Enums.TransactionTypes;
import co.tala.bankaccountmanagement.entities.TransactionEntity;
import co.tala.bankaccountmanagement.utilities.Utilities;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private  TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void canCountByAccountAndDateBetweenAndTransactionType() {

        String accountNo = "12345678";
        double amount = 23.0;
        AccountEntity account = new AccountEntity(accountNo, amount);
        accountRepository.save(account);

        TransactionEntity trx1 = new TransactionEntity(Utilities.generateTransactionId(), amount,
                TransactionTypes.DEPOSIT, account);
        TransactionEntity trx2 = new TransactionEntity(Utilities.generateTransactionId(), amount,
                TransactionTypes.DEPOSIT, account);
        TransactionEntity trx3 = new TransactionEntity(Utilities.generateTransactionId(), amount,
                TransactionTypes.WITHDRAW, account);

        transactionRepository.save(trx1);
        transactionRepository.save(trx2);
        transactionRepository.save(trx3);

        long todayDepositTransactions = transactionRepository.countByAccountAndDateBetweenAndTransactionType(
                account,Utilities.getStartAndEndOfDay().get("startOfDay"), Utilities.getStartAndEndOfDay().get("endOfDay"),
                TransactionTypes.DEPOSIT);

        long todayWithdrawTransactions = transactionRepository.countByAccountAndDateBetweenAndTransactionType(
                account,Utilities.getStartAndEndOfDay().get("startOfDay"), Utilities.getStartAndEndOfDay().get("endOfDay"),
                TransactionTypes.WITHDRAW);

        long allTransactions = todayWithdrawTransactions+todayDepositTransactions;
        assertAll("Transactions Today",
         () ->assertEquals(2, todayDepositTransactions),
         () ->assertEquals(1, todayWithdrawTransactions),
         () ->assertEquals(3, allTransactions));
    }

    @Test
    void canFindAllByAccountAndDateBetweenAndTransactionType() {
        String accountNo = "12345678";
        double amount = 23.0;
        AccountEntity account = new AccountEntity(accountNo, amount);
        accountRepository.save(account);

        TransactionEntity trx1 = new TransactionEntity(Utilities.generateTransactionId(), amount,
                TransactionTypes.DEPOSIT, account);
        TransactionEntity trx2 = new TransactionEntity(Utilities.generateTransactionId(), amount,
                TransactionTypes.DEPOSIT, account);
        TransactionEntity trx3 = new TransactionEntity(Utilities.generateTransactionId(), amount,
                TransactionTypes.WITHDRAW, account);

        transactionRepository.save(trx1);
        transactionRepository.save(trx2);
        transactionRepository.save(trx3);

        List<TransactionEntity> allTransactions = transactionRepository
                .findAllByAccountAndDateBetweenAndTransactionType(account,
                        Utilities.getStartAndEndOfDay().get("startOfDay"),
                        Utilities.getStartAndEndOfDay().get("endOfDay"),
                        TransactionTypes.WITHDRAW);

        assertThat(allTransactions.size()).isEqualTo(1);
    }
}