package com.e_commerce_creator.common.util;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import org.slf4j.LoggerFactory;

public class SeparateLogger {
    public static org.slf4j.Logger addDynamicAppender(String jobName) {
        return addDynamicAppender(jobName, false);
    }

    public static org.slf4j.Logger addDynamicAppender(String jobName, boolean hideLogsFromConsole) {
//        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        //Create a RollingFileAppender
        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<>();
        rollingFileAppender.setContext(context);
        rollingFileAppender.setName(jobName.toUpperCase() + "-APPENDER");//ROLLING

        //Set fileNamePattern dynamically
        String fileNamePattern = "logging-jobs/" + jobName.toLowerCase() + "-job.%d{yyyy-MM-dd}.%i.log";

        // Configure Rolling Policy
        SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
        rollingPolicy.setContext(context);
        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.setFileNamePattern(fileNamePattern);
        rollingPolicy.setMaxFileSize(new FileSize(5 * FileSize.KB_COEFFICIENT));
        rollingPolicy.setMaxHistory(7);
        rollingPolicy.start();

        // Assign the rolling policy to the appender
        rollingFileAppender.setRollingPolicy(rollingPolicy);

        // Configure Encoder
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} %-5level [%thread] %C : %M line-%L - %msg%n");
        encoder.start();

        // Set the encoder for the appender
        rollingFileAppender.setEncoder(encoder);
        rollingFileAppender.start();

        //Attach the appender to the logger
        Logger logger = context.getLogger(jobName);
        logger.addAppender(rollingFileAppender);
        logger.setAdditive(false);
        return LoggerFactory.getLogger(jobName);
    }

    public static String getStackTraceLogger(Exception e, Class<?> _class) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element).append(System.lineSeparator());
            if (element.getClassName().equals(_class.getName())) {
                break;
            }
        }
        return sb.toString();
    }
}