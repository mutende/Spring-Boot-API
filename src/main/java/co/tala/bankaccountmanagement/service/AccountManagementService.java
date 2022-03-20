package co.tala.bankaccountmanagement.service;

import co.tala.bankaccountmanagement.dto.requests.DepositRequest;
import co.tala.bankaccountmanagement.dto.requests.WithdrawRequest;
import co.tala.bankaccountmanagement.pojos.ResourceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AccountManagementService {
    public ResponseEntity<ResourceResponse> getAccountBalance(String accountNo);
    public ResponseEntity<ResourceResponse> depositToAccount(DepositRequest request);
    public ResponseEntity<ResourceResponse> withdraw(WithdrawRequest request);

}
