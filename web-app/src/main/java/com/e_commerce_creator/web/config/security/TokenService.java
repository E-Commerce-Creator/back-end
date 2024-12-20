package com.e_commerce_creator.web.config.security;

import com.e_commerce_creator.common.model.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TokenService {
    final Environment environment;
    final SecretKey secretKey = Keys.hmacShaKeyFor(environment.getProperty("token.key").getBytes(StandardCharsets.UTF_8));

    //1- Read
    private Claims extractAllClaims(String token) {
        //.parser() to read Claims
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //2- Read
    //to extract any data from Claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsFunction.apply(claims);
    }

    //3- Read Username
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //4- Create Token with 3 component (Header , Payload , Signature)
    public String generateToken(Map<String, Object> extraClaims, User user) {
        //.builder() to create token with claims , subject , issuedAt , expiration
        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24)))//24 hr
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    //4- Create Token without claims if needed with another user
    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    //5- implement method that can validate the token
    public boolean isTokenValid(String token, User user) {
        //we need to check if the token belong to user details
        //and also check that the token is not expired
        final String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }


    //6- check if the token is not expired
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

}
