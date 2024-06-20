package org.vino9.vinobank.coresim.casa.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>{
    @Query("SELECT a FROM Account a WHERE a.accountNum = :accountNum and a.status = :status")
    Optional<Account> findByAccountNum(String accountNum, @Param("status") Account.Status status);
}
