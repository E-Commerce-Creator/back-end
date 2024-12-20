package com.e_commerce_creator.common.repository.user;

import com.e_commerce_creator.common.model.user.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("""
            select t 
            from Token t
            inner join User u on t.user.id = u.id
            where u.id = :userId and (t.isExpired = false or t.isRevoked = false) 
            """)
    List<Token> findAllValidTokensByUser_Id(Long id);

    Optional<Token> findByToken(String token);

    Optional<Token> findByTokenAndExpiredFalseAndRevokedFalse(String token);
}
