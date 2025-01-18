package com.e_commerce_creator.common.repository.users;

import com.e_commerce_creator.common.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByNationalId(String nationalId);

    @Query("""
            select case 
            when u.email = :email then 'EMAIL'
            when u.username = :username then 'USERNAME'
            when u.nationalId = :nationalId then 'NATIONAL_ID'
            end as conflictFields
            from User u
            where u.email = :email
            or u.username = :username
            or u.nationalId = :nationalId 
            """)
    Optional<String> findConflictField(String username, String email, String nationalId);
}
