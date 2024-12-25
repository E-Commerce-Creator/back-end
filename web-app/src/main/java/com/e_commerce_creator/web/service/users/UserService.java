package com.e_commerce_creator.web.service.users;

import com.e_commerce_creator.common.model.users.Account;
import com.e_commerce_creator.common.repository.users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    final UserRepository userRepository;


    //CRUD user with specific role OWNER, ADMIN, USER

    public Account getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Account not found :: " + id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findAccountByUsername(username).orElseThrow(() -> new EntityNotFoundException("Failed to load user by username :: " + username));
    }
}
