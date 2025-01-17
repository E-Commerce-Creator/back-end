package com.e_commerce_creator.common.enums.response;

import org.springframework.http.HttpStatus;

/**
 * Define HTTP Status Codes In 100:149, 200:249, 300:349, 400:449, 500:549
 * Define Customs Status Codes In 150:199, 250:299, 350:399, 450:499, 550:599
 */
public enum ResponseCode {
    /**
     * Success
     **/
    SUCCESS(200, "SUCCESS", HttpStatus.OK),
    CREATED(201, "CREATED", HttpStatus.CREATED),
    ACCEPTED(202, "ACCEPTED", HttpStatus.ACCEPTED),
    NO_CONTENT(204, "NO_CONTENT", HttpStatus.NO_CONTENT),
    // Define all custom success responses
    ALREADY_EXIST(250, "ALREADY_EXIST", HttpStatus.CONFLICT),

    /**
     * Client errors
     **/
    BAD_REQUEST(400, "BAD_REQUEST", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(401, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED),
    PAYMENT_REQUIRED(402, "PAYMENT_REQUIRED", HttpStatus.PAYMENT_REQUIRED),
    FORBIDDEN(403, "FORBIDDEN", HttpStatus.FORBIDDEN),
    NOT_FOUND(404, "NOT_FOUND", HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED(405, "METHOD_NOT_ALLOWED", HttpStatus.METHOD_NOT_ALLOWED),
    REQUEST_TIMEOUT(408, "REQUEST_TIMEOUT", HttpStatus.REQUEST_TIMEOUT),
    UNSUPPORTED_MEDIA_TYPE(415, "UNSUPPORTED_MEDIA_TYPE", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    DUPLICATION_CONFLICT(409, "DUPLICATION_CONFLICT", HttpStatus.METHOD_NOT_ALLOWED),
    TASK_ID_NOT_SENT(416, "TASK_ID_NOT_SENT", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(417, "TOKEN_EXPIRED", HttpStatus.FORBIDDEN),


    // Define all custom Profile errors request, Start from 450 To 499
    INVALID_AUTH(450, "INVALID_CREDENTIALS", HttpStatus.FORBIDDEN),
    INVALID_TOKEN(451, "INVALID_TOKEN", HttpStatus.FORBIDDEN),
    DEPRECATED_TOKEN(452, "DEPRECATED_TOKEN_YOU_HAVE_TO_LOGIN", HttpStatus.FORBIDDEN),
    UNSUPPORTED_FILE_TYPE(453, "UNSUPPORTED_FILE_TYPE", HttpStatus.FORBIDDEN),
    EXCEED_MAX_SIZE(454, "EXCEED_MAX_SIZE", HttpStatus.FORBIDDEN),
    NOT_EXIST(455, "NOT_EXIST", HttpStatus.NOT_FOUND),

    /**
     * Server errors
     **/
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_IMPLEMENTED(501, "NOT_IMPLEMENTED", HttpStatus.NOT_IMPLEMENTED),
    DATABASE_ERROR(503, "DATABASE_ERROR", HttpStatus.SERVICE_UNAVAILABLE),


    // Define More Detailed Failure Response, Start from 550 To 599
    NO_DATA_SAVED(501, "NO_DATA_SAVED", HttpStatus.INTERNAL_SERVER_ERROR),
    GENERAL_FAILURE(555, "GENERAL_FAILURE", HttpStatus.INTERNAL_SERVER_ERROR),
    CREATE_ENTITY_FAILURE(558, "CANNOT_CREATE_ENTITY", HttpStatus.INTERNAL_SERVER_ERROR),
    READ_ENTITY_FAILURE(559, "CANNOT_READ_ENTITY", HttpStatus.INTERNAL_SERVER_ERROR),
    UPDATE_ENTITY_FAILURE(560, "CANNOT_UPDATE_ENTITY", HttpStatus.INTERNAL_SERVER_ERROR),
    DELETE_ENTITY_FAILURE(561, "CANNOT_DELETE_ENTITY", HttpStatus.INTERNAL_SERVER_ERROR);


    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ResponseCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    /**
     * Gets the HTTP status code
     *
     * @return the status code number
     */
    public int getCode() {
        return code;
    }

    /**
     * Get the description
     *
     * @return the description of the status code
     */
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }


    /**
     * Get the description
     *
     * @return the description of the status code
     */
    public String getMessageWithPrefix(String prefix) {
        return (prefix != null ? " " + prefix : "") + message;
    }

    /**
     * Get the description
     *
     * @return the description of the status code
     */
    public String getMessageWithPostfix(String postfix) {
        return message + (postfix != null ? " " + postfix : "");
    }
}
