package co.tala.bankaccountmanagement.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
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

    @JoinColumn(name = "transaction_type_id", referencedColumnName = "id",nullable = false)
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private TransactionTypeEntity transactionType;

    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private AccountEntity account;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private Date date;
}
