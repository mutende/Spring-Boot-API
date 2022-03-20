package co.tala.bankaccountmanagement.dto.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class DepositRequest {

    @NotNull(message = "Amount cannot be null")
    @DecimalMax(value = "40000", message = "Maximum deposit for a transaction is $40k")
    private Double amount;

    @NotNull(message = "Account Number  cannot be null")
    @NotEmpty(message = "Account Number cannot  be empty")
    @NotBlank(message = "Account Number cannot  be blank")
    private String accountNo;

}
