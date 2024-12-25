package com.e_commerce_creator.web.service.users;

import com.e_commerce_creator.common.model.users.Account;
import com.e_commerce_creator.common.repository.users.UserRepository;
import com.e_commerce_creator.web.config.security.AuthenticationFilter;
import com.e_commerce_creator.web.config.security.TokenService;
import com.e_commerce_creator.web.dto.request.AuthenticationRequest;
import com.e_commerce_creator.web.dto.request.RegisterRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.parsers.SAXParser;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    final UserRepository userRepository;

    final PasswordEncoder passwordEncoder;
    final TokenService tokenService;
    final AuthenticationManager authenticationManager;

    public String register(RegisterRequest registerRequest) {
        Account account = Account.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();
        Account save = userRepository.save(account);

        return save.getId().toString();
    }

    @Transactional
    public String authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Account account = userRepository.findAccountByUsername(authenticationRequest.getUsername()).orElseThrow(() -> new EntityNotFoundException("account not found"));
        String token = tokenService.generateToken(account);
        return token;
    }
}