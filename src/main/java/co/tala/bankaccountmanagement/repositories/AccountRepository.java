package co.tala.bankaccountmanagement.repositories;

import co.tala.bankaccountmanagement.dto.AccountBalanceDTO;
import co.tala.bankaccountmanagement.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    @Query(value = "select new co.tala.bankaccountmanagement.dto.AccountBalanceDTO (ac.accountNo, ac.amount) from accounts ac where ac.accountNo = :accountNo")
    Optional<AccountBalanceDTO> getAccountBalance(@Param("accountNo") String accountNo);

    Optional<AccountEntity> findAccountEntityByAccountNo(@Param("accountNo") String accountNo);
}
