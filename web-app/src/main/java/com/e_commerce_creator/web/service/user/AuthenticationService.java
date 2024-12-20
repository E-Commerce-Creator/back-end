package com.e_commerce_creator.web.service.user;

import com.e_commerce_creator.common.enums.user.TokenType;
import com.e_commerce_creator.common.model.user.Token;
import com.e_commerce_creator.common.model.user.User;
import com.e_commerce_creator.common.repository.user.TokenRepository;
import com.e_commerce_creator.common.repository.user.UserRepository;
import com.e_commerce_creator.web.config.security.TokenService;
import com.e_commerce_creator.web.dto.request.AuthenticationRequest;
import com.e_commerce_creator.web.dto.request.RegisterRequest;
import com.e_commerce_creator.web.dto.response.AuthenticationResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final TokenService tokenService;
    final AuthenticationManager authenticationManager;
    // we need to save or persist any generated token by our system,
// so we can add it on authentication service that is called by authentication controller
    final TokenRepository tokenRepository;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();
        userRepository.save(user);
        String jwt = tokenService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwt);
        return new AuthenticationResponse(jwt);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //if it passes through this logic mean that the user is authenticated otherwise it will throw exception,
        //so I wil just need to generate the token and then send it back
        User user = userRepository.findUserByUsername(authenticationRequest.getUsername()).orElseThrow(() -> new EntityNotFoundException("user not found"));
        String token = tokenService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, token);
        return new AuthenticationResponse(token);
    }

    private void saveUserToken(User user, String jwt) {
        Token token = Token.builder()
                .user(user)
                .token(jwt)
                .tokenType(TokenType.BEARER_TOKEN)
                .isRevoked(false)
                .isExpired(false)
                .build();

        tokenRepository.saveAndFlush(token);
    }

    private void revokeAllUserTokens(User user) {
        //I want to fetch all the valid tokens for specific user into database
        List<Token> validUserTokens = tokenRepository.findAllValidTokensByUser_Id(user.getId());
        //then revoke these token that are being found
        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach((token) -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }
}