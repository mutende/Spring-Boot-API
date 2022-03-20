package co.tala.bankaccountmanagement.repositories;

import co.tala.bankaccountmanagement.entities.TransactionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionTypeEntity, Long> {
}
