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
    SUCCESS(200, "Success", HttpStatus.OK),
    CREATED(201, "Created", HttpStatus.CREATED),
    ACCEPTED(202, "Accepted", HttpStatus.ACCEPTED),
    NO_CONTENT(204, "No Content", HttpStatus.NO_CONTENT),
    // Define all custom success response, Start from 250 To 299
    ALREADY_EXIST(250, "Is Already Exist", HttpStatus.CONFLICT),
    TASK_ALREADY_CLAIMED(207, "TASK_ALREADY_CLAIMED", HttpStatus.MULTI_STATUS),


    /**
     * Client errors
     **/
    BAD_REQUEST(400, "Bad Request", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(401, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED),
    PAYMENT_REQUIRED(402, "Payment Required", HttpStatus.PAYMENT_REQUIRED),
    FORBIDDEN(403, "Forbidden", HttpStatus.FORBIDDEN),
    NOT_FOUND(404, "Not Found", HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", HttpStatus.METHOD_NOT_ALLOWED),
    REQUEST_TIMEOUT(408, "Request Timeout", HttpStatus.REQUEST_TIMEOUT),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    LOGIN_ERROR(499, "Method Not Allowed", HttpStatus.METHOD_NOT_ALLOWED),
    DUPLICATION_CONFLICT(409, "The request could not be completed as item is already exists", HttpStatus.METHOD_NOT_ALLOWED),
    TASK_ID_NOT_SENT(416, "Task Id is not sent", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(417, "Token Expired", HttpStatus.FORBIDDEN),


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
    CORDYS_FAILURE(502, "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_IMPLEMENTED(501, "NOT_IMPLEMENTED", HttpStatus.NOT_IMPLEMENTED),
    DATABASE_ERROR(503, "SERVICE_UNAVAILABLE", HttpStatus.SERVICE_UNAVAILABLE),


    // Define More Detailed Failure Response, Start from 550 To 599
    NO_DATA_SAVED(501, "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    GENERAL_FAILURE(555, "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    QUERY_BUILDER_FAILURE(556, "QUERY_BUILDER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    MODULE_ROUTING_FAILURE(557, "MODULE_ROUTING_FAILURE", HttpStatus.INTERNAL_SERVER_ERROR),
    CREATE_ENTITY_FAILURE(558, "CANNOT_CREATE_ENTITY", HttpStatus.INTERNAL_SERVER_ERROR),
    READ_ENTITY_FAILURE(559, "CANNOT_READ_ENTITY", HttpStatus.INTERNAL_SERVER_ERROR),
    UPDATE_ENTITY_FAILURE(560, "CANNOT_UPDATE_ENTITY", HttpStatus.INTERNAL_SERVER_ERROR),
    DELETE_ENTITY_FAILURE(561, "CANNOT_DELETE_ENTITY", HttpStatus.INTERNAL_SERVER_ERROR),
    MODULE_ROUTING_INPUTS_ERROR(566, "INVALID_INPUTS", HttpStatus.INTERNAL_SERVER_ERROR),
    APP_BUILDER_FAILURE(562, "APP_BUILDER_FAILURE", HttpStatus.INTERNAL_SERVER_ERROR),
    CREATE_FILE_FAILURE(563, "CREATE_FILE_FAILURE", HttpStatus.INTERNAL_SERVER_ERROR),
    ADD_RELATION_TO_ENTITY_FAILURE(564, "CANNOT_ADD_RELATION_TO_ENTITY", HttpStatus.INTERNAL_SERVER_ERROR),
    DELETE_RELATION_TO_ENTITY_FAILURE(565, "CANNOT_DELETE_RELATION_TO_ENTITY", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_GROUP_FAILURE(566, "CANNOT_FIND_LOGGED_IN_USER_GROUP", HttpStatus.INTERNAL_SERVER_ERROR),
    READ_FILE_FAILURE(567, "READ_FILE_FAILURE", HttpStatus.INTERNAL_SERVER_ERROR);


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
