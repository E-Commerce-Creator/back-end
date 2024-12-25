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

        //1- we want to invalidate the token, so we need to extract the token from request right there
        //2- we need to fetch this request in database and invalidated
        //3- then the JwtAuthenticationFilter will do the job since we update our mechanism or our implementation there

        final String header = request.getHeader("X-Auth-Token");
        final String jwt;

        if (header != null && header.startsWith("Bearer ")) {
            jwt = header.substring(7);
            System.out.println("jwt :: " + jwt);
            //todo remove Token entity we need to get current token handler and do our logic for logout
        }

    }
}
