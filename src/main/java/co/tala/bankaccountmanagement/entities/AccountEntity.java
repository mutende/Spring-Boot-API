package co.tala.bankaccountmanagement.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="accounts")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "account_no", nullable = false)
    private String accountNo;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_created", insertable = false, updatable = false)
    private Date dateCreated;
}
