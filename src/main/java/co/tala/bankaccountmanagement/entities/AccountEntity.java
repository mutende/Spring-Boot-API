package co.tala.bankaccountmanagement.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity(name="accounts")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "account_no", nullable = false)
    private String accountNo;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @CreationTimestamp
    @Column(name="date_created", nullable = false, updatable = false)
    private Date dateCreated;

    public AccountEntity(String accountNo, Double amount) {
        this.accountNo = accountNo;
        this.amount = amount;
    }
}
