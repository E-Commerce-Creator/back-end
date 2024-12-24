package com.e_commerce_creator.web.config;

import com.e_commerce_creator.web.config.security.AuthenticationFilter;
import com.e_commerce_creator.web.service.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
//@ComponentScan(basePackages = {"com.e_commerce_creator.common", "com.e_commerce_creator.web"})
@ComponentScan("com.e_commerce_creator")
public class ApplicationConfig {


}
