package org.vino9.vinobank.coresim.casa.data;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>{
    @Query("SELECT a FROM Account a WHERE a.accountNum = :accountNum and a.status = 'ACTIVE'")
    Optional<Account> findByAccountNum(String accountNum);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT a FROM Account a WHERE a.accountNum in (:debitAccountNum, :creditAccountNum) and a.status = 'ACTIVE'")
    List<Account> findByAccountsForTransfer(String debitAccountNum, String creditAccountNum);
}
