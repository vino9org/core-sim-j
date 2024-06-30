package org.vino9.vinobank.coresim.casa.data;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "casa_transaction", indexes = {
        @Index(name = "idx_account_trx_date", columnList = "account_id, trx_date")
})
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "trx_date", length = 10)
    private String trxDate;

    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "amount", precision = 14, scale = 2)
    private BigDecimal amount;

    @Column(name = "running_balance", precision = 14, scale = 2)
    private BigDecimal runningBalance;

    @Column(name = "ref_id", length = 32)
    private String refId;

    @Column(name = "trx_id", length = 32)
    private String trxId;

    @Column(name = "memo", length = 100)
    private String memo;

    @Column(name = "is_published")
    private Boolean isPublished = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Getters and Setters
}