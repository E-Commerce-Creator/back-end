package com.e_commerce_creator.web.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;

        if (header != null && header.startsWith("Bearer ")) {
            jwt = header.substring(7);
            System.out.println("jwt :: " + jwt);
            //todo remove Token entity we need to get current token handler and do our logic for logout
        }

    }
}
