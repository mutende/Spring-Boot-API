package co.tala.bankaccountmanagement.entities;


import co.tala.bankaccountmanagement.entities.Enums.TypesOfTransaction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.EnumType;


import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name="transaction_types")
public class TransactionTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_types")
    private TypesOfTransaction type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_created", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private Date dateCreated;
}
