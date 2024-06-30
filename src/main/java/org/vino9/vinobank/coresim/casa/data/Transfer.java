package org.vino9.vinobank.coresim.casa.data;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "casa_transfer")
@Data
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "trx_id", length = 32, unique = true)
    private String trxId;

    @Column(name = "trx_date", length = 10)
    private String trxDate;

    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "amount", precision = 14, scale = 2)
    private BigDecimal amount;

    @Column(name = "ref_id", length = 32)
    private String refId;

    @Column(name = "memo", length = 100)
    private String memo;

    @Column(name = "debit_account_num", length = 32)
    private String debitAccountNum;

    @Column(name = "credit_account_num", length = 32)
    private String creditAccountNum;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}