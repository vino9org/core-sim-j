package org.vino9.vinobank.coresim.casa.data;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "casa_transfer")
@Data
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ref_id", length = 32, nullable = false, unique = true)
    private String refId;

    @Column(name = "trx_date", length = 10, nullable = false)
    private String trxDate;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @Column(name = "amount", precision = 14, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "memo", length = 100)
    private String memo;

    @Column(name = "debit_account_num", length = 32, nullable = false)
    private String debitAccountNum;

    @Column(name = "credit_account_num", length = 32, nullable = false)
    private String creditAccountNum;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
