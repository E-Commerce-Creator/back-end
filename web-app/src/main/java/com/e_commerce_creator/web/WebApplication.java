package com.e_commerce_creator.web;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.util.TimeZone;

@SpringBootApplication
public class WebApplication {
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    private static ApplicationContext applicationContext = null;

    public static void main(String[] args) throws IOException {

        String mode = args != null && args.length > 0 ? args[0] : null;

        if (applicationContext != null && mode != null && "stop".equals(mode)) {
            System.exit(SpringApplication.exit(applicationContext, new ExitCodeGenerator() {
                @Override
                public int getExitCode() {
                    return 0;
                }
            }));
        }
        else {
            SpringApplication app = new SpringApplication(WebApplication.class);
            applicationContext = app.run(args);
        }
    }
}