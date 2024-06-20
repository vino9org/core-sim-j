package org.vino9.vinobank.coresim.casa.data;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "casa_account", uniqueConstraints = @UniqueConstraint(columnNames = {"account_num", "currency"}))
@Data
public class Account {

    public enum Status {
        active("active"),
        suspended("suspended"),
        closed("closed");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_num", length = 32, nullable = false)
    private String accountNum;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @Column(name = "balance", precision = 14, scale = 2, nullable = false)
    private BigDecimal balance;

    @Column(name = "avail_balance", precision = 14, scale = 2, nullable = false)
    private BigDecimal availBalance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10, nullable = false)
    private Status status = Status.active;

    @Column(name = "updated_at", nullable = false, updatable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();
}
