package com.e_commerce_creator.web.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.e_commerce_creator.common.enums.response.ResponseCode;
import com.e_commerce_creator.common.exception.ECCException;
import com.e_commerce_creator.common.model.users.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

@Service
public class TokenService {
    final Environment environment;
    final SecretKey secretKey;

    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    @Autowired
    public TokenService(Environment environment) {
        this.environment = environment;
        String tokenKey = environment.getProperty("token.key");
        if (tokenKey.isBlank()) {
            throw new IllegalStateException("Token key is not configured in the environment");
        }
        this.secretKey = Keys.hmacShaKeyFor(tokenKey.getBytes(StandardCharsets.UTF_8));
    }

    //end point token generator (other algorithm)
    public String generate(User user) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonSubject = "";
        try {
            jsonSubject = mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
        }
        return JWT.create()
                .withSubject(jsonSubject)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(Objects.requireNonNull(environment.getProperty("token.key")).getBytes()));
    }

    //get user without token validation
    public User get(String token) {
        ObjectMapper mapper = new ObjectMapper();
        User user = null;
        try {
            String jsonSubject = JWT.require(Algorithm.HMAC512(Objects.requireNonNull(environment.getProperty("token.key")).getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();
            user = mapper.readValue(jsonSubject, User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return user;
    }

    //get user with token validation
    public User getAndValidate(String token) throws ECCException {
        User user = get(token);
        if (isTokenExpired(token) || !isTokenValid(token, user)) throw new ECCException(ResponseCode.TOKEN_EXPIRED);
        else return user;
    }

    public boolean isTokenValid(String token, User user) {
        try {
            String subject = JWT.require(Algorithm.HMAC512(environment.getProperty("token.key").getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();
            ObjectMapper mapper = new ObjectMapper();
            User tokenUser = mapper.readValue(subject, User.class);
            return (
                    tokenUser.getUsername().equals(user.getUsername()) && tokenUser.getEmail().equals(user.getEmail())
            ) && !isTokenExpired(token);
        } catch (Exception e) {
            // Handle exceptions and consider the token invalid
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiresAt = JWT.decode(token).getExpiresAt();
            return expiresAt.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

}
