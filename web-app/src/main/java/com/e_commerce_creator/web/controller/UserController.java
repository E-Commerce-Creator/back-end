package com.e_commerce_creator.web.controller;

import com.e_commerce_creator.common.enums.response.ResponseCode;
import com.e_commerce_creator.common.enums.user.Permission;
import com.e_commerce_creator.common.enums.user.Role;
import com.e_commerce_creator.common.model.account.Account;
import com.e_commerce_creator.common.util.SystemUtils;
import com.e_commerce_creator.web.config.security.TokenService;
import com.e_commerce_creator.web.response.AppResponse;
import com.e_commerce_creator.web.service.users.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("api/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    final TokenService tokenService;

    final UserService userService;


    @GetMapping("details")
    public ResponseEntity<AppResponse<JsonNode>> getUserDetails(
            @RequestHeader("X-Auth-Token") String token
    ) {
        AppResponse.ResponseBuilder<JsonNode> responseBuilder = AppResponse.builder();
        try {
            Account account = tokenService.getAndValidate(token);
            if (account == null) return responseBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();

            responseBuilder.data(
                    SystemUtils.convertStringToJsonNode(
                            userService.getUserDetails(account)
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseBuilder.info("errorMessage", e.getMessage());
            responseBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build().getResponseEntity();
    }

    @PutMapping("change/role/{id}")
    public ResponseEntity<AppResponse<JsonNode>> changeRole(@RequestHeader("X-Auth-Token") String token, @PathVariable Long id, @RequestBody ObjectNode node) {
        AppResponse.ResponseBuilder<JsonNode> responseBuilder = AppResponse.builder();
        try {
            Account account = tokenService.getAndValidate(token);
            if (account == null) return responseBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            userService.changeRole(id, Role.valueOf(node.get("role").asText()));

            responseBuilder.info("message", "Role changed successfully");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseBuilder.info("errorMessage", e.getMessage());
            responseBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build().getResponseEntity();
    }

    @PutMapping("change/permission/{id}")
    public ResponseEntity<AppResponse<JsonNode>> changePermission(@RequestHeader("X-Auth-Token") String token, @PathVariable Long id, @RequestBody ObjectNode node) {
        AppResponse.ResponseBuilder<JsonNode> responseBuilder = AppResponse.builder();
        try {
            Account account = tokenService.getAndValidate(token);
            if (account == null) return responseBuilder.status(ResponseCode.UNAUTHORIZED).build().getResponseEntity();
            userService.addOrRemovePermission(id, Permission.valueOf(node.get("permission").asText()), node.has("add") && node.get("add").asBoolean());

            responseBuilder.info("message", "Permission changed successfully");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseBuilder.info("errorMessage", e.getMessage());
            responseBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build().getResponseEntity();
    }
}
