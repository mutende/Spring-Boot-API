package co.tala.bankaccountmanagement.controller;

import co.tala.bankaccountmanagement.dto.requests.DepositRequest;
import co.tala.bankaccountmanagement.dto.requests.WithdrawRequest;
import co.tala.bankaccountmanagement.pojos.ResourceResponse;
import co.tala.bankaccountmanagement.service.AccountManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/")
public class AccountManagementController {

    private final AccountManagementService accountManagementService;

    public AccountManagementController(AccountManagementService accountManagementService) {
        this.accountManagementService = accountManagementService;
    }


    @PostMapping("/getAccountBalance/{accountNo}")
    public ResponseEntity<ResourceResponse> getAccountBalance(@PathVariable String accountNo)
    {
        return accountManagementService.getAccountBalance(accountNo);
    }

    @PostMapping("/deposit")
    public ResponseEntity<ResourceResponse> depositToAccount(@Valid @RequestBody DepositRequest request)
    {
        return accountManagementService.depositToAccount(request);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ResourceResponse> withdraw(@Valid @RequestBody WithdrawRequest request)
    {
        return accountManagementService.withdraw(request);
    }

}
