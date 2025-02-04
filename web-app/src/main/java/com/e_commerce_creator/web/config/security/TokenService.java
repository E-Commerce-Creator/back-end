package com.e_commerce_creator.web.config.security;

import com.e_commerce_creator.common.model.account.Account;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class TokenService {
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days
    final Environment environment;
    final SecretKey secretKey;

    @Autowired
    public TokenService(Environment environment) {
        this.environment = environment;
        String tokenKey = environment.getProperty("token.key");
        if (tokenKey.isBlank()) {
            throw new IllegalStateException("Token key is not configured in the environment");
        }
        this.secretKey = Keys.hmacShaKeyFor(tokenKey.getBytes(StandardCharsets.UTF_8));
    }


    //1- Read
    private Claims extractAllClaims(String token) {
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

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public ObjectNode extractExtraClaims(String token) {
        Claims claims = extractAllClaims(token);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.convertValue(claims, ObjectNode.class);
        objectNode.remove("sub");
        objectNode.remove("iat");
        objectNode.remove("exp");
        objectNode.remove("authorities");
//        List<String> permissions = new ArrayList<>();
//        if (objectNode.get("authorities").isArray()) {
//            ArrayNode arrayNode = (ArrayNode) objectNode.get("authorities");
//            permissions = mapper.convertValue(arrayNode, ArrayList.class);
//        }
//        List<SimpleGrantedAuthority> authorities = permissions.stream().map(SimpleGrantedAuthority::new).toList();
//        ArrayNode arrayNode = mapper.createArrayNode();
//        authorities.forEach(authority -> arrayNode.add(mapper.convertValue(authority, JsonNode.class)));
//        objectNode.set("authorities", arrayNode);
        return objectNode;
    }


    //3- Read
    public Account extractAccount(String token) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = extractExtraClaims(token);
        return mapper.convertValue(objectNode, Account.class);
    }

    //4- Create Token with 3 component (Header , Payload , Signature)
    public String generateToken(Account account) {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode objectNode = mapper.valueToTree(account);

        objectNode.remove(List.of("accountNonLocked", "accountNonExpired", "credentialsNonExpired", "enabled", "authorities"));

        List<String> authorities = account.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        ArrayNode authoritiesArray = objectNode.putArray("authorities");
        authorities.forEach(authoritiesArray::add);

        return generateToken(objectNode, account);
    }

    public String generateToken(ObjectNode extraClaims, Account account) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> mappedClaims = mapper.convertValue(extraClaims, new TypeReference<>() {});
        return Jwts.builder()
                .claims(mappedClaims)
                .subject(account.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }


    //5- implement method that can validate the token
    public boolean isTokenValid(String token, Account account) {
        //we need to check if the token belong to account details
        //and also check that the token is not expired
        final String username = extractUsername(token);
        return username.equals(account.getUsername()) && !isTokenExpired(token);
    }

    //6- check if the token is not expired
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public Account getAndValidate(String token) {
//        if (token.startsWith("Bearer ")) token = token.substring(7);
        Account account = extractAccount(token);
        return isTokenValid(token, account) ? account : null;
    }

    public Account get(String token) {
        if (token.startsWith("Bearer ")) token.substring(7);
        return extractAccount(token);
    }

}
