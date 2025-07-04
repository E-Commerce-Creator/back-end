package com.e_commerce_creator.web.config.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

import static com.e_commerce_creator.common.enums.user.Role.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    final AuthenticationFilter authenticationFilter;

    final LogoutService logoutService;


    //At the application, startup spring security will try to look for bean of type SecurityFilterChain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //do the configuration
        return httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/api/authenticate/**").permitAll()
                                .requestMatchers("/api/user/**").hasAnyRole(USER.name(), ADMIN.name())
//                                .requestMatchers(HttpMethod.GET, "api/v1/owner").hasAnyAuthority(Permission.ADMIN_READ.name(), Permission.OWNER_READ.name())
//                                .requestMatchers(HttpMethod.POST, "api/v1/owner").hasAnyAuthority(Permission.ADMIN_CREATE.name(), Permission.OWNER_CREATE.name())
//                                .requestMatchers(HttpMethod.PUT, "api/v1/owner").hasAnyAuthority(Permission.ADMIN_UPDATE.name(), Permission.OWNER_UPDATE.name())
//                                .requestMatchers(HttpMethod.DELETE, "api/v1/owner").hasAnyAuthority(Permission.ADMIN_DELETE.name(), Permission.OWNER_DELETE.name())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.addAllowedOriginPattern("*");
                        config.setAllowCredentials(true);
                        return config;
                    }
                }))
                .addFilterBefore(authenticationFilter, BasicAuthenticationFilter.class)
                .logout(logout -> {
                    logout.logoutUrl("/api/v1/auth/logout");
                    logout.addLogoutHandler(logoutService);
                    logout.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
                })
                .build();
    }
}


