package com.e_commerce_creator.web.config.security;

import com.e_commerce_creator.common.model.users.Account;
import com.e_commerce_creator.common.repository.users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


//1
//Tell Spring that it will be Managed Bean
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    final TokenService tokenService;
    final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //any issue on spring security start to debug from AuthenticationFilter.doFilterInternal
        //prepare the Bearer token that are sent with request on authorization http header
        final String authHeader = request.getHeader("X-Auth-Token");
        final String token;
        final String username;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            //todo extract the username from Jwt token JwtService;
            try {
                username = tokenService.extractUsername(token);
            } catch (Exception e) {
                logger.error("Hossam :: Invalid JWT Token", e);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
            //check if the user is not authenticated yet to continue the JWT validation process
            //if user is already authenticate then we not need to go through the JWT validation Process
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //we need to get the account from database
                Account account = userRepository.findAccountByUsername(username).orElseThrow(() -> new EntityNotFoundException("account not found"));

                if (tokenService.isTokenValid(token, account)) {
                    // Update Security Context Holder to set Authentication true
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities());
                    //I want to give some more details about the http request
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //final step is to update the security context holder
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        //don't forget always to pass to next filter to be executed
        filterChain.doFilter(request, response);
    }
}
