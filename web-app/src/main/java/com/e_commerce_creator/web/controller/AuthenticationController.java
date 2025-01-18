package com.e_commerce_creator.web.controller;

import com.e_commerce_creator.common.enums.response.ResponseCode;
import com.e_commerce_creator.common.exception.ECCException;
import com.e_commerce_creator.common.util.SystemUtils;
import com.e_commerce_creator.web.dto.request.AuthenticationRequest;
import com.e_commerce_creator.web.dto.request.RegisterRequest;
import com.e_commerce_creator.web.response.AppResponse;
import com.e_commerce_creator.web.service.users.AuthenticationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("api/authenticate")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    final AuthenticationService authenticationService;


    @PostMapping(value = "/register")
    public ResponseEntity<AppResponse<String>> register(@RequestBody RegisterRequest request) {
        AppResponse.ResponseBuilder<String> responseBuilder = AppResponse.builder();
        try {
            responseBuilder.data(
                    authenticationService.register(request)
            );
            responseBuilder.status(ResponseCode.SUCCESS);

        } catch (ECCException e) {
            log.error(e.getMessage());
            responseBuilder.info("errorMessage", e.getMessage());
            responseBuilder.status(e.getCode());
        } catch (Exception e) {
            log.error(e.getMessage());
            responseBuilder.info("errorMessage", e.getMessage());
            responseBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build().getResponseEntity();
    }

    @PostMapping("login")
    public ResponseEntity<AppResponse<JsonNode>> authenticate(@RequestBody AuthenticationRequest request) {
        AppResponse.ResponseBuilder<JsonNode> responseBuilder = AppResponse.builder();
        try {
            ObjectNode node = authenticationService.authenticate(request);
            responseBuilder.data(SystemUtils.convertStringToJsonNode(node));
            responseBuilder.status(ResponseCode.SUCCESS);
        } catch (ECCException e) {
            log.error(e.getMessage());
            responseBuilder.info("errorMessage", e.getMessage());
            responseBuilder.status(e.getCode());
        } catch (Exception e) {
            log.error(e.getMessage());
            responseBuilder.info("errorMessage", e.getMessage());
            responseBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build().getResponseEntity();
    }

}
