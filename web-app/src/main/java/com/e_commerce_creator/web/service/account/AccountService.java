package com.e_commerce_creator.web.service.account;

import com.e_commerce_creator.common.repository.account.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
    static final String EMAIL_REGEX = "^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        boolean isEmailFormat = Pattern.matches(EMAIL_REGEX, usernameOrEmail);
        if (isEmailFormat) {
            return accountRepository.findAccountByEmail(usernameOrEmail).orElseThrow(() -> new EntityNotFoundException("user not found"));
        } else {
            return accountRepository.findAccountByUsername(usernameOrEmail).orElseThrow(() -> new EntityNotFoundException("user not found"));
        }
    }
}
