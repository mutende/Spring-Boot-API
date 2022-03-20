package co.tala.bankaccountmanagement.repositories;

import co.tala.bankaccountmanagement.dto.AccountBalanceDTO;
import co.tala.bankaccountmanagement.entities.AccountEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    void getAccountBalance() {
        String accountNo = "12345678";
        double amount = 23.0;
        AccountEntity account = new AccountEntity(accountNo, amount);
        accountRepository.save(account);

        Optional<AccountBalanceDTO> accountBalanceDTO = accountRepository.getAccountBalance(accountNo);

        assertThat(accountBalanceDTO.isPresent());
        assertThat(accountBalanceDTO.get().getAccountNo()).isEqualTo(accountNo);
        assertThat(accountBalanceDTO.get().getBalance()).isEqualTo(amount);

        double newBal = amount + 10;
        System.out.println(account);
        account.setAmount(newBal);
        accountRepository.save(account);
        accountBalanceDTO = accountRepository.getAccountBalance(accountNo);
        assertThat(accountBalanceDTO.get().getBalance()).isEqualTo(newBal);
        assertThat(accountBalanceDTO.get().getBalance()).isGreaterThan(amount);
    }

    @Test
    void findAccountEntityByAccountNo() {

        String accountNo = "12345678";
        double amount = 23.0;
        AccountEntity account = new AccountEntity(accountNo, amount);
        accountRepository.save(account);

        Optional<AccountEntity> ac = accountRepository.findAccountEntityByAccountNo(accountNo);

        assertAll("Account Details",
                () -> assertEquals(true, ac.isPresent()),
                () -> assertEquals("12345678", ac.get().getAccountNo()),
                () -> assertEquals(23.0,  ac.get().getAmount())
        );
    }
}