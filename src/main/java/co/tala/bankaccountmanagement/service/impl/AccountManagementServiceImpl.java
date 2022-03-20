package co.tala.bankaccountmanagement.service.impl;

import co.tala.bankaccountmanagement.dto.AccountBalanceDTO;
import co.tala.bankaccountmanagement.dto.requests.DepositRequest;
import co.tala.bankaccountmanagement.dto.requests.WithdrawRequest;
import co.tala.bankaccountmanagement.pojos.ResourceResponse;
import co.tala.bankaccountmanagement.repositories.AccountRepository;
import co.tala.bankaccountmanagement.repositories.TransactionRepository;
import co.tala.bankaccountmanagement.service.AccountManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
//        Optional<AccountBalanceDTO> accountBalanceDTO = accountRepository.getAccountBalance(accountNo);
//
//        if(accountBalanceDTO.isEmpty()){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body( new ResourceResponse(
//                    "Account number does not exist", HttpStatus.NOT_FOUND.value(),null
//            ));
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body( new ResourceResponse(
//                "Account balance request processed successfully", HttpStatus.OK.value(),accountBalanceDTO.get()
//        ));

        return null;
    }

    @Override
    public ResponseEntity<ResourceResponse> depositToAccount(DepositRequest request) {
        //
        return null;
    }

    @Override
    public ResponseEntity<ResourceResponse> withdraw(WithdrawRequest request) {
        return null;
    }
}
