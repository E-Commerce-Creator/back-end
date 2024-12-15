package com.e_commerce_creator.common.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AppConfig {

    final Environment environment;

    @Value("${spring.profiles.active}")
    String activeProfile;

    @PostConstruct
    public void logActiveProfile() {
        log.info("Currently active profile access from SpEL - {}", activeProfile);
    }

    //todo
    @Profile("dev")
    public void dev() {
        log.info("Currently active profile access from property file - {}", environment.getProperty("spring.datasource.username"));
        String databaseUrl = environment.getProperty("spring.datasource.url");
        log.info("Currently active profile access on database : ({})", databaseUrl.substring(databaseUrl.lastIndexOf("/") + 1, databaseUrl.indexOf("?")));
    }

    @Profile("test")
    public void test() {
        log.info("Currently active profile access from property file - {}", environment.getProperty("spring.datasource.username"));
        String databaseUrl = environment.getProperty("spring.datasource.url");
        log.info("Currently active profile access on database : ({})", databaseUrl.substring(databaseUrl.lastIndexOf("/") + 1, databaseUrl.indexOf("?")));
    }

    @Profile("prod")
    public void prod() {
        log.info("Currently active profile access from property file - {}", environment.getProperty("spring.datasource.username"));
        String databaseUrl = environment.getProperty("spring.datasource.url");
        log.info("Currently active profile access on database : ({})", databaseUrl.substring(databaseUrl.lastIndexOf("/") + 1, databaseUrl.indexOf("?")));
    }


}
