package co.tala.bankaccountmanagement.entities;


import co.tala.bankaccountmanagement.entities.Enums.TransactionTypes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity(name="transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "transaction_id", updatable = false, nullable = false)
    private String transactionId;

    @Column(name = "amount", nullable = false, columnDefinition = "double default 0.00")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionTypes type;

    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private AccountEntity account;

    @CreationTimestamp
    @Column(name="date", nullable = false, updatable = false)
    private Date date;

    public TransactionEntity(String transactionId, Double amount, TransactionTypes type, AccountEntity account) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.account = account;
    }
}
