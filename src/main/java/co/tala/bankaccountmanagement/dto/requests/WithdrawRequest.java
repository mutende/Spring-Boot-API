package co.tala.bankaccountmanagement.dto.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class WithdrawRequest {

    @NotNull(message = "Amount cannot be null")
    @DecimalMax(value = "20000", message = "“Exceeded Maximum Withdrawal Per Transaction")
    private Double amount;

    @NotNull(message = "Account Number  cannot be null")
    @NotEmpty(message = "Account Number cannot  be empty")
    @NotBlank(message = "Account Number cannot  be blank")
    private String accountNo;
}
