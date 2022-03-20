package co.tala.bankaccountmanagement.repositories;

import co.tala.bankaccountmanagement.entities.AccountEntity;
import co.tala.bankaccountmanagement.entities.Enums.TransactionTypes;
import co.tala.bankaccountmanagement.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    Long countByAccountAndDateBetweenAndTransactionType(AccountEntity account, Date startDate, Date endDate,
                                                        TransactionTypes transactionType);

    List<TransactionEntity> findAllByAccountAndDateBetweenAndTransactionType(AccountEntity account, Date startDate,
                                                                              Date endDate,
                                                                             TransactionTypes transactionType);
}
