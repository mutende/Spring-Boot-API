package co.tala.bankaccountmanagement.service.impl;

import co.tala.bankaccountmanagement.dto.AccountBalanceDTO;
import co.tala.bankaccountmanagement.dto.requests.DepositRequest;
import co.tala.bankaccountmanagement.dto.requests.WithdrawRequest;
import co.tala.bankaccountmanagement.entities.AccountEntity;
import co.tala.bankaccountmanagement.entities.Enums.TransactionTypes;
import co.tala.bankaccountmanagement.entities.TransactionEntity;
import co.tala.bankaccountmanagement.pojos.ResourceResponse;
import co.tala.bankaccountmanagement.repositories.AccountRepository;
import co.tala.bankaccountmanagement.repositories.TransactionRepository;
import co.tala.bankaccountmanagement.service.AccountManagementService;
import co.tala.bankaccountmanagement.utilities.Utilities;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class AccountManagementServiceImpl implements AccountManagementService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountManagementServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public ResponseEntity<ResourceResponse> getAccountBalance(String accountNo) {
        Optional<AccountBalanceDTO> accountBalanceDTO = accountRepository.getAccountBalance(accountNo);

        if(accountBalanceDTO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body( new ResourceResponse(
                    "Account number does not exist", HttpStatus.NOT_FOUND.value(),null
            ));
        }
        return ResponseEntity.status(HttpStatus.OK).body( new ResourceResponse(
                "Account balance request processed successfully", HttpStatus.OK.value(),accountBalanceDTO.get()
        ));
    }

    @Override
    public ResponseEntity<ResourceResponse> depositToAccount(DepositRequest request) {
        Optional<AccountEntity> account = accountRepository.findAccountEntityByAccountNo(request.getAccountNo());

        //start of day
        Date date = new Date();
        LocalDateTime localDateTime = Utilities.dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);


        if(account.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body( new ResourceResponse(
                    "Account number does not exist", HttpStatus.NOT_FOUND.value(),null
            ));
        }

        long numberOfTransactionsToday = transactionRepository.countByAccountAndDateBetween(account.get(),
                Utilities.localDateTimeToDate(startOfDay), Utilities.localDateTimeToDate(endOfDay));


        if((numberOfTransactionsToday+1) > 4)
        {
            return ResponseEntity.status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED).body( new ResourceResponse(
                    "You have reached limit of your transactions today ", HttpStatus.BANDWIDTH_LIMIT_EXCEEDED.value(), null
            ));
        }


        List<TransactionEntity> todaysTransactions = transactionRepository.findAllByAccountAndDateBetween(account.get(),
                Utilities.localDateTimeToDate(startOfDay), Utilities.localDateTimeToDate(endOfDay));


        double totalAmountDepositedToday = todaysTransactions.stream().mapToDouble(TransactionEntity::getAmount).sum();

        if((totalAmountDepositedToday + request.getAmount()) > 150000)
        {
            return ResponseEntity.status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED).body( new ResourceResponse(
                    "You have reached limit of amount you can deposit in a day, total deposits for today is  $"+
                            totalAmountDepositedToday+" and daily limit is $150k",
                    HttpStatus.BANDWIDTH_LIMIT_EXCEEDED.value(), null
            ));
        }

        TransactionEntity trx = new TransactionEntity(Utilities.generateTransactionId(), request.getAmount(),
                TransactionTypes.DEPOSIT, account.get());
        transactionRepository.save(trx);
        double currentBalance = account.get().getAmount();
        account.get().setAmount(currentBalance + request.getAmount());
        accountRepository.save(account.get());

        return ResponseEntity.status(HttpStatus.CREATED).body( new ResourceResponse(
                "Successfully deposited "+request.getAmount()+" to your account", HttpStatus.CREATED.value(), null
        ));
    }

    @Override
    public ResponseEntity<ResourceResponse> withdraw(WithdrawRequest request) {
        return null;
    }
}
