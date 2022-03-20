package co.tala.bankaccountmanagement.configs;

import co.tala.bankaccountmanagement.entities.AccountEntity;
import co.tala.bankaccountmanagement.repositories.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AccountSeeder implements CommandLineRunner {

    private final AccountRepository accountRepository;

    public AccountSeeder(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... args) {
        AccountEntity account =new AccountEntity("1122334455", 0.00);
        accountRepository.save(account);

    }
}
