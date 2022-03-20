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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration(classes = {AccountManagementServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class AccountManagementServiceImplTest {

    @Autowired
    private AccountManagementServiceImpl accountManagementServiceImpl;

    @Autowired
    private AccountManagementServiceImpl accountManagementService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;


    @BeforeEach
    void setUp() {
        accountManagementService = new AccountManagementServiceImpl(accountRepository, transactionRepository);
    }


    @Test
    void shouldReturnNotFoundGetAccountBalance() {
        String accountNo = "6677889933";
        accountManagementService.getAccountBalance(accountNo);

        ArgumentCaptor<String> accountNoArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(accountRepository).getAccountBalance(accountNoArgumentCaptor.capture());
        String accountCaptured = accountNoArgumentCaptor.getValue();
        assertThat(accountCaptured).isEqualTo(accountNo);

        ResponseEntity<ResourceResponse> expectedRes = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResourceResponse(
                "Account number does not exist", HttpStatus.NOT_FOUND.value(), null
        ));

        assertThat(accountManagementService.getAccountBalance(accountNo).getStatusCode()).isEqualTo(expectedRes.getStatusCode());

    }


    @Test
    void shouldReturnOkGetAccountBalance() {
        String accountNo = "6677889955";
        double amount = 23.0;

        AccountBalanceDTO acc = new AccountBalanceDTO(accountNo, amount);

        when(accountRepository.getAccountBalance(accountNo)).thenReturn(Optional.of(acc));

        ResponseEntity<ResourceResponse> expectedRes = ResponseEntity.status(HttpStatus.OK).body(
                new ResourceResponse("Account balance request processed successfully", HttpStatus.OK.value(), acc
                ));

        assertThat(accountManagementService.getAccountBalance(accountNo).getStatusCode()).isEqualTo(expectedRes.getStatusCode());
    }

    @Test
    void whenAccountIsInvalidDepositToAccount() {

        String accountNo = "6677889933";
        double amount = 55.89;

        DepositRequest r = new DepositRequest();

        r.setAmount(amount);
        r.setAccountNo(accountNo);
        accountManagementService.depositToAccount(r);

        ArgumentCaptor<String> accountNoArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(accountRepository).findAccountEntityByAccountNo(accountNoArgumentCaptor.capture());
        String accountCaptured = accountNoArgumentCaptor.getValue();
        assertThat(accountCaptured).isEqualTo(accountNo);

        ResponseEntity<ResourceResponse> expectedRes = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResourceResponse(
                "Account number does not exist", HttpStatus.NOT_FOUND.value(), null
        ));

        assertThat(accountManagementService.depositToAccount(r).getStatusCode()).isEqualTo(expectedRes.getStatusCode());

    }


    @Test
    void testDepositToAccount() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountNo("3");
        accountEntity.setAmount(10.0d);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        accountEntity.setDateCreated(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        accountEntity.setId(123L);

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAccount(accountEntity);
        transactionEntity.setAmount(10.0d);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        transactionEntity.setDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        transactionEntity.setId(123L);
        transactionEntity.setTransactionId("42");
        transactionEntity.setTransactionType(TransactionTypes.DEPOSIT);
        when(transactionRepository.countByAccountAndDateBetweenAndTransactionType((AccountEntity) any(), (Date) any(),
                (Date) any(), (TransactionTypes) any())).thenReturn(3L);
        when(transactionRepository.save((TransactionEntity) any())).thenReturn(transactionEntity);
        when(transactionRepository.findAllByAccountAndDateBetweenAndTransactionType((AccountEntity) any(),
                (Date) any(), (Date) any(), (TransactionTypes) any())).thenReturn(new ArrayList<>());

        AccountEntity accountEntity1 = new AccountEntity();
        accountEntity1.setAccountNo("3");
        accountEntity1.setAmount(10.0d);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        accountEntity1.setDateCreated(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        accountEntity1.setId(123L);
        Optional<AccountEntity> ofResult = Optional.of(accountEntity1);

        AccountEntity accountEntity2 = new AccountEntity();
        accountEntity2.setAccountNo("3");
        accountEntity2.setAmount(10.0d);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        accountEntity2.setDateCreated(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        accountEntity2.setId(123L);
        when(accountRepository.save((AccountEntity) any())).thenReturn(accountEntity2);
        when(accountRepository.findAccountEntityByAccountNo((String) any())).thenReturn(ofResult);

        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setAccountNo("3");
        depositRequest.setAmount(10.0d);
        ResponseEntity<ResourceResponse> actualDepositToAccountResult = accountManagementService
                .depositToAccount(depositRequest);
        assertTrue(actualDepositToAccountResult.hasBody());
        assertTrue(actualDepositToAccountResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualDepositToAccountResult.getStatusCode());
        ResourceResponse body = actualDepositToAccountResult.getBody();
        assertNull(body.getBody());
        assertEquals("Successfully deposited $10.0 to your account", body.getMessage());
        assertEquals(200, body.getStatus());
        verify(transactionRepository).countByAccountAndDateBetweenAndTransactionType((AccountEntity) any(),
                (Date) any(), (Date) any(), (TransactionTypes) any());
        verify(transactionRepository).save((TransactionEntity) any());
        verify(transactionRepository).findAllByAccountAndDateBetweenAndTransactionType((AccountEntity) any(),
                (Date) any(), (Date) any(), (TransactionTypes) any());
        verify(accountRepository).save((AccountEntity) any());
        verify(accountRepository).findAccountEntityByAccountNo((String) any());
    }

    @Test
    void testDepositToAccount2() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountNo("3");
        accountEntity.setAmount(10.0d);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        accountEntity.setDateCreated(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        accountEntity.setId(123L);

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAccount(accountEntity);
        transactionEntity.setAmount(10.0d);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        transactionEntity.setDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        transactionEntity.setId(123L);
        transactionEntity.setTransactionId("42");
        transactionEntity.setTransactionType(TransactionTypes.DEPOSIT);
        when(transactionRepository.countByAccountAndDateBetweenAndTransactionType((AccountEntity) any(), (Date) any(),
                (Date) any(), (TransactionTypes) any())).thenReturn(1L);
        when(transactionRepository.save((TransactionEntity) any())).thenReturn(transactionEntity);
        when(transactionRepository.findAllByAccountAndDateBetweenAndTransactionType((AccountEntity) any(),
                (Date) any(), (Date) any(), (TransactionTypes) any())).thenReturn(new ArrayList<>());

        AccountEntity accountEntity1 = new AccountEntity();
        accountEntity1.setAccountNo("3");
        accountEntity1.setAmount(10.0d);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        accountEntity1.setDateCreated(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        accountEntity1.setId(123L);
        Optional<AccountEntity> ofResult = Optional.of(accountEntity1);

        AccountEntity accountEntity2 = new AccountEntity();
        accountEntity2.setAccountNo("3");
        accountEntity2.setAmount(10.0d);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        accountEntity2.setDateCreated(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        accountEntity2.setId(123L);
        when(accountRepository.save((AccountEntity) any())).thenReturn(accountEntity2);
        when(accountRepository.findAccountEntityByAccountNo((String) any())).thenReturn(ofResult);

        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setAccountNo("3");
        depositRequest.setAmount(10.0d);
        ResponseEntity<ResourceResponse> actualDepositToAccountResult = accountManagementService
                .depositToAccount(depositRequest);
        assertTrue(actualDepositToAccountResult.hasBody());
        assertTrue(actualDepositToAccountResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualDepositToAccountResult.getStatusCode());
        ResourceResponse body = actualDepositToAccountResult.getBody();
        assertNull(body.getBody());
        assertEquals("Successfully deposited $10.0 to your account", body.getMessage());
        assertEquals(200, body.getStatus());
        verify(transactionRepository).countByAccountAndDateBetweenAndTransactionType((AccountEntity) any(),
                (Date) any(), (Date) any(), (TransactionTypes) any());
        verify(transactionRepository).save((TransactionEntity) any());
        verify(transactionRepository).findAllByAccountAndDateBetweenAndTransactionType((AccountEntity) any(),
                (Date) any(), (Date) any(), (TransactionTypes) any());
        verify(accountRepository).save((AccountEntity) any());
        verify(accountRepository).findAccountEntityByAccountNo((String) any());
    }

    @Test
    void testDepositToAccount3() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountNo("3");
        accountEntity.setAmount(10.0d);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        accountEntity.setDateCreated(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        accountEntity.setId(123L);

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAccount(accountEntity);
        transactionEntity.setAmount(10.0d);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        transactionEntity.setDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        transactionEntity.setId(123L);
        transactionEntity.setTransactionId("42");
        transactionEntity.setTransactionType(TransactionTypes.DEPOSIT);
        when(transactionRepository.countByAccountAndDateBetweenAndTransactionType((AccountEntity) any(), (Date) any(),
                (Date) any(), (TransactionTypes) any())).thenReturn(4L);
        when(transactionRepository.findAllByAccountAndDateBetweenAndTransactionType((AccountEntity) any(),
                (Date) any(), (Date) any(), (TransactionTypes) any())).thenReturn(new ArrayList<>());

        AccountEntity accountEntity1 = new AccountEntity();
        accountEntity1.setAccountNo("3");
        accountEntity1.setAmount(10.0d);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        accountEntity1.setDateCreated(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        accountEntity1.setId(123L);
        Optional<AccountEntity> ofResult = Optional.of(accountEntity1);

        AccountEntity accountEntity2 = new AccountEntity();
        accountEntity2.setAccountNo("3");
        accountEntity2.setAmount(10.0d);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        accountEntity2.setDateCreated(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        accountEntity2.setId(123L);
        when(accountRepository.findAccountEntityByAccountNo((String) any())).thenReturn(ofResult);

        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setAccountNo("3");
        depositRequest.setAmount(10.0d);
        ResponseEntity<ResourceResponse> actualDepositToAccountResult = accountManagementService
                .depositToAccount(depositRequest);
        assertTrue(actualDepositToAccountResult.hasBody());
        assertTrue(actualDepositToAccountResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, actualDepositToAccountResult.getStatusCode());
        ResourceResponse body = actualDepositToAccountResult.getBody();
        assertNull(body.getBody());
        assertEquals("You have reached limit of your deposit transactions today ", body.getMessage());
        assertEquals(509, body.getStatus());
        verify(transactionRepository).countByAccountAndDateBetweenAndTransactionType((AccountEntity) any(),
                (Date) any(), (Date) any(), (TransactionTypes) any());
        verify(transactionRepository).findAllByAccountAndDateBetweenAndTransactionType((AccountEntity) any(),
                (Date) any(), (Date) any(), (TransactionTypes) any());
        verify(accountRepository).findAccountEntityByAccountNo((String) any());
    }

    @Test
    void testWithdraw() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountNo("3");
        accountEntity.setAmount(10.0d);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        accountEntity.setDateCreated(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        accountEntity.setId(123L);

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAccount(accountEntity);
        transactionEntity.setAmount(10.0d);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        transactionEntity.setDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        transactionEntity.setId(123L);
        transactionEntity.setTransactionId("42");
        transactionEntity.setTransactionType(TransactionTypes.WITHDRAW);
        when(transactionRepository.countByAccountAndDateBetweenAndTransactionType((AccountEntity) any(), (Date) any(),
                (Date) any(), (TransactionTypes) any())).thenReturn(3L);
        when(transactionRepository.findAllByAccountAndDateBetweenAndTransactionType((AccountEntity) any(),
                (Date) any(), (Date) any(), (TransactionTypes) any())).thenReturn(new ArrayList<>());

        AccountEntity accountEntity1 = new AccountEntity();
        accountEntity1.setAccountNo("3");
        accountEntity1.setAmount(10.0d);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        accountEntity1.setDateCreated(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        accountEntity1.setId(123L);
        Optional<AccountEntity> ofResult = Optional.of(accountEntity1);
        when(accountRepository.findAccountEntityByAccountNo((String) any())).thenReturn(ofResult);

        WithdrawRequest withdrawRequest = new WithdrawRequest();
        withdrawRequest.setAccountNo("3");
        withdrawRequest.setAmount(10.0d);
        ResponseEntity<ResourceResponse> actualWithdrawResult = accountManagementService.withdraw(withdrawRequest);
        assertTrue(actualWithdrawResult.hasBody());
        assertTrue(actualWithdrawResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, actualWithdrawResult.getStatusCode());
        ResourceResponse body = actualWithdrawResult.getBody();
        assertNull(body.getBody());
        assertEquals("You have reached limit of your withdrawal transactions today", body.getMessage());
        assertEquals(509, body.getStatus());
        verify(transactionRepository).countByAccountAndDateBetweenAndTransactionType((AccountEntity) any(),
                (Date) any(), (Date) any(), (TransactionTypes) any());
        verify(transactionRepository).findAllByAccountAndDateBetweenAndTransactionType((AccountEntity) any(),
                (Date) any(), (Date) any(), (TransactionTypes) any());
        verify(accountRepository).findAccountEntityByAccountNo((String) any());
    }

    @Test
    void testWithdraw2() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountNo("3");
        accountEntity.setAmount(10.0d);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        accountEntity.setDateCreated(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        accountEntity.setId(123L);

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAccount(accountEntity);
        transactionEntity.setAmount(10.0d);
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        transactionEntity.setDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        transactionEntity.setId(123L);
        transactionEntity.setTransactionId("42");
        transactionEntity.setTransactionType(TransactionTypes.WITHDRAW);
        when(transactionRepository.countByAccountAndDateBetweenAndTransactionType((AccountEntity) any(), (Date) any(),
                (Date) any(), (TransactionTypes) any())).thenReturn(1L);
        when(transactionRepository.save((TransactionEntity) any())).thenReturn(transactionEntity);
        when(transactionRepository.findAllByAccountAndDateBetweenAndTransactionType((AccountEntity) any(),
                (Date) any(), (Date) any(), (TransactionTypes) any())).thenReturn(new ArrayList<>());

        AccountEntity accountEntity1 = new AccountEntity();
        accountEntity1.setAccountNo("3");
        accountEntity1.setAmount(10.0d);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        accountEntity1.setDateCreated(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        accountEntity1.setId(123L);
        Optional<AccountEntity> ofResult = Optional.of(accountEntity1);

        AccountEntity accountEntity2 = new AccountEntity();
        accountEntity2.setAccountNo("3");
        accountEntity2.setAmount(10.0d);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        accountEntity2.setDateCreated(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        accountEntity2.setId(123L);
        when(accountRepository.save((AccountEntity) any())).thenReturn(accountEntity2);
        when(accountRepository.findAccountEntityByAccountNo((String) any())).thenReturn(ofResult);

        WithdrawRequest withdrawRequest = new WithdrawRequest();
        withdrawRequest.setAccountNo("3");
        withdrawRequest.setAmount(10.0d);
        ResponseEntity<ResourceResponse> actualWithdrawResult = accountManagementService.withdraw(withdrawRequest);
        assertTrue(actualWithdrawResult.hasBody());
        assertTrue(actualWithdrawResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualWithdrawResult.getStatusCode());
        ResourceResponse body = actualWithdrawResult.getBody();
        assertNull(body.getBody());
        assertEquals("Successfully withdrawn $10.0 from your account", body.getMessage());
        assertEquals(200, body.getStatus());
        verify(transactionRepository).countByAccountAndDateBetweenAndTransactionType((AccountEntity) any(),
                (Date) any(), (Date) any(), (TransactionTypes) any());
        verify(transactionRepository).save((TransactionEntity) any());
        verify(transactionRepository).findAllByAccountAndDateBetweenAndTransactionType((AccountEntity) any(),
                (Date) any(), (Date) any(), (TransactionTypes) any());
        verify(accountRepository).save((AccountEntity) any());
        verify(accountRepository).findAccountEntityByAccountNo((String) any());
    }

}