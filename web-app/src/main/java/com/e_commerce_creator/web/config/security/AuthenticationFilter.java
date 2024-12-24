package com.e_commerce_creator.web.config.security;

import com.e_commerce_creator.common.enums.response.ResponseCode;
import com.e_commerce_creator.common.exception.ECCException;
import com.e_commerce_creator.common.model.users.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends GenericFilterBean {

    //Lazily injecting beans
    final ObjectProvider<AuthenticationManager> authenticationManagerProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("LOG: AuthenticationFilter doFilter Start.");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Optional<String> token = Optional.ofNullable(httpRequest.getHeader("X-Auth-Token"));

        try {
            if (token.isPresent() && !((String) token.get()).isEmpty()) {
                processTokenAuthentication(token);
            }

            log.info("LOG: AuthenticationFilter doFilter END.");
            chain.doFilter(request, response);
        } catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
            log.info("LOG: AuthenticationFilter internalAuthenticationServiceException.");
            SecurityContextHolder.clearContext();
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (AuthenticationException authenticationException) {
            log.info("LOG: AuthenticationFilter authenticationException.");
            SecurityContextHolder.clearContext();
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
        }
    }

    public void processTokenAuthentication(Optional<String> token) {
        log.info("LOG: AuthenticationFilter processTokenAuthentication START.");
        AuthenticationManager authenticationManager = authenticationManagerProvider.getObject();
        PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(token, null);
        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials");
        }
        log.info("LOG: AuthenticationFilter processTokenAuthentication END.");
        SecurityContextHolder.getContext().setAuthentication(responseAuthentication);
    }

    public static class AuthenticationWithToken extends PreAuthenticatedAuthenticationToken {

        public AuthenticationWithToken(Object aPrincipal, Object aCredentials, Collection<? extends GrantedAuthority> anAuthorities) {
            super(aPrincipal, aCredentials, anAuthorities);
        }

        public void setToken(String token) {
            setDetails(token);
        }
    }

    //token Authentication Provider
    @Slf4j
    @Service
    public static class TokenAuthenticationProvider implements AuthenticationProvider {

        @Autowired
        TokenService tokenService;

        @SneakyThrows
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            log.info("LOG: TokenAuthenticationProvider authenticate START.");
            Optional token = (Optional) authentication.getPrincipal();
            if (!token.isPresent()) throw new BadCredentialsException("Invalid token");

            Authentication tokenAuthentication = getAuthentication((String) token.get());
            log.info("LOG: TokenAuthenticationProvider authenticate END.");
            if (tokenAuthentication == null) throw new BadCredentialsException("Invalid token or token expired");
            return tokenAuthentication;
        }

        public Authentication getAuthentication(String token) throws ECCException {
            try {
                log.info("LOG: TokenAuthenticationProvider getAuthentication START.");
                User account = tokenService.get(token);
                if (account == null) throw new ECCException("Invalid User", ResponseCode.INVALID_AUTH);
                AuthenticationFilter.AuthenticationWithToken resultOfAuthentication = new AuthenticationFilter.AuthenticationWithToken(account.getUsername(), null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_DOMAIN_USER"));
                resultOfAuthentication.setToken(token);
                log.info("LOG: TokenAuthenticationProvider getAuthentication END.");
                return resultOfAuthentication;
            } catch (Exception e) {
                throw new ECCException(e.getMessage(), ResponseCode.INTERNAL_SERVER_ERROR);
            }
        }

        @Override
        public boolean supports(Class<?> authentication) {
            return authentication.equals(PreAuthenticatedAuthenticationToken.class);
        }
    }

}
