package org.vino9.vinobank.coresim.casa.data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "casa_account", indexes = {
        @Index(name = "idx_account_num_status", columnList = "account_num, status")
})

@Data
public class Account {
    @Getter
    public enum StatusEnum {
        ACTIVE("ACTIVE"),
        SUSPENDED("SUSPENDED"),
        CLOSED("CLOSED");

        private final String value;

        StatusEnum(String value) {
            this.value = value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "account_num", length = 16)
    private String accountNum;

    @Column(name = "currency", length = 20)
    private String currency = "USD";

    @Column(name = "balance", precision = 14, scale = 2)
    private BigDecimal balance;

    @Column(name = "avail_balance", precision = 14, scale = 2)
    private BigDecimal availBalance;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StatusEnum status = StatusEnum.ACTIVE;
}
