package co.tala.bankaccountmanagement.controller;

import co.tala.bankaccountmanagement.dto.requests.DepositRequest;
import co.tala.bankaccountmanagement.dto.requests.WithdrawRequest;
import co.tala.bankaccountmanagement.pojos.ResourceResponse;
import co.tala.bankaccountmanagement.service.AccountManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/v1/")
public class AccountManagementController {

    private final AccountManagementService accountManagementService;

    public AccountManagementController(AccountManagementService accountManagementService) {
        this.accountManagementService = accountManagementService;
    }


    @PostMapping("/get-account-balance/{accountNo}")
    public ResponseEntity<ResourceResponse> getAccountBalance(@PathVariable String accountNo)
    {
        return accountManagementService.getAccountBalance(accountNo);
    }

    @PostMapping("/deposit")
    public ResponseEntity<ResourceResponse> depositToAccount(@Valid DepositRequest request)
    {
        return accountManagementService.depositToAccount(request);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ResourceResponse> withdraw(@Valid WithdrawRequest request)
    {
        return accountManagementService.withdraw(request);
    }

}
