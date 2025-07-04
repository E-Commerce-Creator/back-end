package com.e_commerce_creator.common.repository.account;

import com.e_commerce_creator.common.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByUsername(String username);

    Optional<Account> findAccountByEmail(String email);

}