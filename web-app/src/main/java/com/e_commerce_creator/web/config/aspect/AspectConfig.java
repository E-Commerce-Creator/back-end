package com.e_commerce_creator.web.config.aspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

public class AspectConfig {

}

@Component
@Slf4j
class Interceptor {
//    @Component
//    @Aspect
//    @RequiredArgsConstructor
//    public static class Logging {
//
//
//        final AuditCreateAsyncTask auditCreateAsyncTask;
//
//        final EntityManager entityManager;
//
//        @Around("execution(* com.asset.appwork.controller.*.*(..))")
//        public Object inWebLayer(ProceedingJoinPoint joinPoint) throws Throwable {
//            log.info("LOG: Interceptor Web Layer Start.");
//            Statistics statistics = entityManager.unwrap(Session.class).getSessionFactory().getStatistics();
//
//            statistics.clear();
//            long startTime = 0, endTime = 0;
//            Object object = null; // before
//            try {
//                startTime = System.currentTimeMillis();
//                if (!joinPoint.getSignature().getName().equals("login")) {
//                    log.info(
//                            "Invoked: {} with {}",
//                            niceName(joinPoint),
//                            auditCreateAsyncTask.generateAuditDetails(joinPoint, false)
//                    );
//                } else {
//                    log.info(
//                            "Invoked: {}",
//                            niceName(joinPoint)
//                    );
//                }
//                object = joinPoint.proceed();
//
//                try {
//                    log.info("LOG: CALL CREATE AUDIT.");
//                    createAudit(joinPoint, object, RequestContextHolder.getRequestAttributes());
//                    log.info("LOG: AFTER CALL CREATE AUDIT.");
//                } catch (Exception exception) {
//                    log.error("Couldn't Add Audit Record");
//                }
//
//                endTime = System.currentTimeMillis();
//            } finally {
//                log.info(
//                        "Finished: {} in {} ms with {} executed queries.",
//                        niceName(joinPoint),
//                        (endTime - startTime),
//                        statistics.getPrepareStatementCount()
//                );
//
//                statistics.clear();
//            }
//
//            log.info("LOG: Interceptor Web Layer END.");
//            return object;
//        }
//
//        private String niceName(JoinPoint joinPoint) {
//            List<String> classPath = Arrays.asList(joinPoint.getTarget().getClass().getName().split("\\."));
//            return classPath.get(classPath.size() - 1) + "#" + joinPoint.getSignature().getName();
//        }
//
//        private void createAudit(JoinPoint joinPoint, Object object, RequestAttributes requestAttributes) {
//            auditCreateAsyncTask.createAudit(joinPoint, object, requestAttributes);
//        }
//    }
//
//
//    /**
//     * Aspect for intercept any repository call
//     */
////    @Aspect
////    @Component
////    public static class RepositoryInterceptor {
////
////
////        @Around("execution(* org.springframework.data.repository.Repository+.*(..))")
////        public Object inWebLayer(ProceedingJoinPoint joinPoint) throws Throwable {
//////            Object object = null; // before
//////            try {
//////                object = joinPoint.proceed();
//////            } finally {
//////
//////            }
////            return joinPoint.proceed();
////
////        }
////    }
//}
//
//@Slf4j
//@Component
//class AuditCreateAsyncTask {
////    @Autowired
////    TokenService tokenService;
////    @Autowired
////    OrgChartService orgChartService;
////    @Autowired
////    AuditService auditService;
//
//    @Async
//    public void createAudit(JoinPoint joinPoint, Object object, RequestAttributes requestAttributes) {
//        Audit audit = new Audit();
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        String auditDetails = "";
//        Optional<Action> actionAnnotation = Optional.ofNullable(method.getAnnotation(Action.class));
//        AtomicReference<String> actionName = new AtomicReference<>("");
//        AtomicReference<ActionAdditionalDetails> actionAdditionalDetails = new AtomicReference<>(ActionAdditionalDetails.NONE);
//        actionAnnotation.ifPresent(action -> {
//            actionName.set(action.name());
//            actionAdditionalDetails.set(action.additionalDetails());
//        });
//
//        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(requestAttributes)).getRequest();
//        Optional<String> token = Optional.ofNullable(request.getHeader("X-Auth-Token"));
//        try {
//            if (actionName.get().isEmpty()) return;
//
//            String userId = null;
//            String responseCode = null;
//            try {
//                if (token.isPresent()) {
//                    userId = tokenService.getAndValidate(token.get()).getUserId();
//                }
//                responseCode = ((ResponseEntity) object).getStatusCode().toString();
//            } catch (AppworkException e) {
//                userId = tokenService.get(token.get()).getUserId();
//                responseCode = e.getCode().getHttpStatus().toString();
//            } catch (Exception e) {
//                if (token.isPresent())
//                    userId = tokenService.get(token.get()).getUserId();
//                responseCode = "500 INTERNAL_SERVER_ERROR";
//            }
//
//            audit.setUserId(userId);
//            audit.setTakenAction(actionName.get());
//            audit.setResponseCode(responseCode);
//            if (!joinPoint.getSignature().getName().equals("login")) {
//                auditDetails = generateAuditDetails(joinPoint);
//                audit.setDetails(auditDetails);
//
//                if (audit.getTakenAction().contains("advanced-search")) {
//                    String takeAction = audit.getTakenAction() + new JSONObject(auditDetails).getString("status");
//                    audit.setTakenAction(takeAction);
//                }
//
//                if (audit.getTakenAction().contains("attachment") && audit.getResponseCode().contains("500"))
//                    audit.setAdditionalDetails(((ResponseEntity) object).getBody().toString());
//                if (actionAdditionalDetails.get().equals(ActionAdditionalDetails.RESPONSE_BODY)) {
//                    Object responseData =
//                            ((AppResponse) Objects.requireNonNull(((ResponseEntity) object).getBody()))
//                                    .getData();
//
//                    String identifier = "";
//
//                    try {
//                        identifier =
//                                responseData.getClass().getDeclaredMethod("getId").invoke(responseData)
//                                        .toString();
//                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
//                        identifier = String.valueOf(
//                                (responseData instanceof ObjectNode) ?
//                                        ((ObjectNode) responseData).get("id") :
//                                        responseData
//                        );
//                    }
//
//                    String oldResponseData = "";
//                    List<Audit> userAuditList =
//                            auditService.getAuditListByTakenActionsUserIdsAndIdentifiers(audit.getTakenAction(),
//                                    audit.getUserId(),
//                                    identifier);
//                    if (!userAuditList.isEmpty()) {
//                        oldResponseData = userAuditList.get(0).getAdditionalDetails();
//                    }
//
//
//                    ObjectMapper mapper = new ObjectMapper();
//                    if (mapper.readTree(responseData.toString()).equals(mapper.readTree(oldResponseData))) return;
//
//                    audit.setAdditionalDetails(
//                            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
//                                    responseData
//                            )
//                    );
//                    audit.setIdentifier(identifier);
//                }
//            }
//
//            auditService.createAdminAudit(audit);
//        } catch (IOException | AppworkException | JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    String generateAuditDetails(JoinPoint joinPoint, Boolean usePrettyPrinter) throws IOException {
//        log.info("LOG: GENERATE AUDIT DETAILS IN INTERCEPTOR START.");
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//
//        JsonFactory factory = new JsonFactory();
//        StringWriter jsonObjectWriter = new StringWriter();
//        JsonGenerator jsonGenerator = factory.createGenerator(jsonObjectWriter);
//        if (usePrettyPrinter) {
//            jsonGenerator.useDefaultPrettyPrinter();
//        }
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
////                mapper.registerModule(new JsonOrgModule());
////                mapper.registerModule(new JSR353Module());
////                mapper.registerModule(new JodaMoneyModule());
////                mapper.registerModule(new JSONPModule());
//        jsonGenerator.setCodec(mapper);
//        jsonGenerator.writeStartObject();
//        for (int i = 0; i < method.getParameters().length; i++) {
//            String parameterName = method.getParameters()[i].getName();
//            if (!parameterName.equals("token") && !parameterName.equals("page") && !parameterName.equals("size") &&
//                    !parameterName.equals("sortBy") && !parameterName.equals("sortDesc") && !parameterName.equals("fields")
//            ) {
//                try {
//                    jsonGenerator.writeObjectField(parameterName, objectToAuditDetails(joinPoint.getArgs()[i]));
//                } catch (JsonGenerationException | InvalidDefinitionException e) {
//                    log.error("Could not Serialize Object/Property of parameter name: " + parameterName);
//                }
//            }
//        }
//        jsonGenerator.writeEndObject();
//        jsonGenerator.close();
//        log.info("LOG: GENERATE AUDIT DETAILS IN INTERCEPTOR END.");
//        return jsonObjectWriter.toString();
//    }
//
//    String generateAuditDetails(JoinPoint joinPoint) throws IOException {
//        return generateAuditDetails(joinPoint, true);
//    }
//
//    Object objectToAuditDetails(Object obj) {
//        log.info("LOG: objectToAuditDetails IN INTERCEPTOR START.");
//        if (obj instanceof java.util.Optional) {
//            Optional<?> optional = ((Optional<?>) obj);
//            if (optional.isPresent())
//                return optional.get();
//            else return "";
//        } else if (obj instanceof com.asset.appwork.dto.Account) {
//            ((Account) obj).setPassword(null);
//            ((Account) obj).setTicket(null);
//            ((Account) obj).setSAMLart(null);
//            return obj;
//        } else if (obj instanceof com.spring.schemas.LoginRequest) {
//            ((LoginRequest) obj).setPassword(null);
//            return obj;
//        }
//        log.info("LOG: objectToAuditDetails IN INTERCEPTOR END.");
//        return obj;
//    }
}
