package org.vino9.vinobank.coresim.casa.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>{
    @Query("SELECT a FROM Account a WHERE a.accountNum = :accountNum and a.status = 'ACTIVE'")
    Optional<Account> findByAccountNum(String accountNum);
}
