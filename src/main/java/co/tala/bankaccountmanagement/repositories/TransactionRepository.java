package co.tala.bankaccountmanagement.repositories;

import co.tala.bankaccountmanagement.entities.AccountEntity;
import co.tala.bankaccountmanagement.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    Long countByAccountAndDateBetween(AccountEntity account, Date startDate, Date endDate);

    List<TransactionEntity> findAllByAccountAndDateBetween(AccountEntity account, Date startDate, Date endDate);
}
