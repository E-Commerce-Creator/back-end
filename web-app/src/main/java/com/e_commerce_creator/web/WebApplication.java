package com.e_commerce_creator.web;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.TimeZone;

@SpringBootApplication
public class WebApplication {
    /**
     * The code provides a mechanism to stop the Spring Boot application gracefully based on a specific command-line argument.
     * The idea is that if you run the application with the "stop" argument (e.g., java -jar your-app.jar stop),
     * the application will shut down cleanly without needing to kill the process manually.
     **/
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
        } else {
            SpringApplication app = new SpringApplication(WebApplication.class);
            applicationContext = app.run(args);
        }
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}