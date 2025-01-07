package com.e_commerce_creator.web.config.aspect;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class AspectConfig {
    /**
     * <h1><b>Aspect for intercept any controller call</b></h1>
     */
    @Component
    @Aspect
    @RequiredArgsConstructor
    public static class ControllerInterceptor {


        final EntityManager entityManager;

        @Around("execution(* com.e_commerce_creator.web.controller.*.*(..))")
        public Object inWebLayer(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("LOG: Interceptor Web Layer Start.");
            Statistics statistics = entityManager.unwrap(Session.class).getSessionFactory().getStatistics();

            statistics.clear();
            long startTime = 0, endTime = 0;
            Object object = null; // before
            try {
                startTime = System.currentTimeMillis();
                if (!joinPoint.getSignature().getName().equals("login")) {
                    log.info(
                            "Invoked: {}",
                            niceName(joinPoint)
                    );
                } else {
                    log.info(
                            "Invoked: {}",
                            niceName(joinPoint)
                    );
                }
                object = joinPoint.proceed();

                try {
                    log.info("LOG: CALL CREATE AUDIT.");
                    log.info("LOG: AFTER CALL CREATE AUDIT.");
                } catch (Exception exception) {
                    log.error("Couldn't Add Audit Record");
                }

                endTime = System.currentTimeMillis();
            } finally {
                log.info(
                        "Finished: {} in {} ms with {} executed queries.",
                        niceName(joinPoint),
                        (endTime - startTime),
                        statistics.getPrepareStatementCount()
                );

                statistics.clear();
            }

            log.info("LOG: Interceptor Web Layer END.");
            return object;
        }

        private String niceName(JoinPoint joinPoint) {
            List<String> classPath = Arrays.asList(joinPoint.getTarget().getClass().getName().split("\\."));
            return classPath.get(classPath.size() - 1) + "#" + joinPoint.getSignature().getName();
        }

    }


    /**
     * <h1><b>Aspect for intercept any repository call</b></h1>
     */
    @Aspect
    @Component
    public static class RepositoryInterceptor {

//        @Around("execution(* org.springframework.data.repository.Repository+.*(..))")
//        public Object inWebLayer(ProceedingJoinPoint joinPoint) throws Throwable {
//            Object object = null; // before
//            try {
//                object = joinPoint.proceed();
//            } finally {
//
//            }
//            return joinPoint.proceed();
//        }
    }
}