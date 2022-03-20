package co.tala.bankaccountmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AccountBalanceDTO {

    private String AccountNo;
    private Double balance;
}
