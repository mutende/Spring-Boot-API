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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


@Service
@Transactional
public class AccountManagementServiceImpl implements AccountManagementService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountManagementServiceImpl(AccountRepository accountRepository,
                                        TransactionRepository transactionRepository) {
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

        if(account.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body( new ResourceResponse(
                    "Account number does not exist", HttpStatus.NOT_FOUND.value(),null
            ));
        }

        List<TransactionEntity> todayTransactions = transactionRepository
                .findAllByAccountAndDateBetweenAndTransactionType(account.get(),
                        Utilities.getStartAndEndOfDay().get("startOfDay"),
                        Utilities.getStartAndEndOfDay().get("endOfDay"),
                        TransactionTypes.DEPOSIT);


        long numberOfTransactionsToday = transactionRepository
                .countByAccountAndDateBetweenAndTransactionType(account.get(),
                        Utilities.getStartAndEndOfDay().get("startOfDay"),
                        Utilities.getStartAndEndOfDay().get("endOfDay"), TransactionTypes.DEPOSIT)
                + 1;


        if(numberOfTransactionsToday > 4) {
            return ResponseEntity.status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED).body( new ResourceResponse(
                    "You have reached limit of your deposit transactions today ",
                    HttpStatus.BANDWIDTH_LIMIT_EXCEEDED.value(), null
            ));
        }


        double totalAmountDepositedToday = todayTransactions.stream().mapToDouble(TransactionEntity::getAmount).sum();

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

        return ResponseEntity.status(HttpStatus.OK).body( new ResourceResponse(
                "Successfully deposited $"+request.getAmount()+" to your account",
                HttpStatus.OK.value(), null
        ));
    }

    @Override
    public ResponseEntity<ResourceResponse> withdraw(WithdrawRequest request) {

        Optional<AccountEntity> account = accountRepository.findAccountEntityByAccountNo(request.getAccountNo());

        if(account.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body( new ResourceResponse(
                    "Account number does not exist", HttpStatus.NOT_FOUND.value(),null
            ));
        }

        if(request.getAmount() > account.get().getAmount())
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body( new ResourceResponse(
                    "You do not have sufficient account balance to complete this transaction",
                    HttpStatus.FORBIDDEN.value(), null
            ));
        }

        List<TransactionEntity> todayTransactions = transactionRepository
                .findAllByAccountAndDateBetweenAndTransactionType(account.get(),
                        Utilities.getStartAndEndOfDay().get("startOfDay"),
                        Utilities.getStartAndEndOfDay().get("endOfDay"),
                        TransactionTypes.WITHDRAW);

        long numberOfTransactionsToday = transactionRepository.countByAccountAndDateBetweenAndTransactionType(account.get(),
                Utilities.getStartAndEndOfDay().get("startOfDay"), Utilities.getStartAndEndOfDay().get("endOfDay"),
                TransactionTypes.WITHDRAW)
                + 1;

        if(numberOfTransactionsToday > 3) {
            return ResponseEntity.status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED).body( new ResourceResponse(
                    "You have reached limit of your withdrawal transactions today",
                    HttpStatus.BANDWIDTH_LIMIT_EXCEEDED.value(), null
            ));
        }

        double totalAmountWithdrawnToday = todayTransactions.stream().mapToDouble(TransactionEntity::getAmount).sum();

        if((totalAmountWithdrawnToday + request.getAmount()) > 50000)
        {
            return ResponseEntity.status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED).body( new ResourceResponse(
                    "You have reached limit of amount you can withdraw in a day, " +
                            "total amount withdrawn today is $"+ totalAmountWithdrawnToday+" and daily limit is $50k",
                    HttpStatus.BANDWIDTH_LIMIT_EXCEEDED.value(), null
            ));
        }

        TransactionEntity trx = new TransactionEntity(Utilities.generateTransactionId(), request.getAmount(),
                TransactionTypes.WITHDRAW, account.get());
        transactionRepository.save(trx);
        double currentBalance = account.get().getAmount();
        account.get().setAmount(currentBalance - request.getAmount());
        accountRepository.save(account.get());

        return ResponseEntity.status(HttpStatus.OK).body( new ResourceResponse(
                "Successfully withdrawn $"+request.getAmount()+" from your account",
                HttpStatus.OK.value(), null
        ));
    }


}
