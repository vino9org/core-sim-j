package org.vino9.vinobank.coresim.casa.data;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.account = :account")
    List<Transaction> findByTransactionsByAccount(Account account);
}
