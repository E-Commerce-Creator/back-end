package com.e_commerce_creator.web.controller;

import com.e_commerce_creator.common.enums.response.ResponseCode;
import com.e_commerce_creator.common.util.SystemUtil;
import com.e_commerce_creator.web.dto.request.AuthenticationRequest;
import com.e_commerce_creator.web.dto.request.RegisterRequest;
import com.e_commerce_creator.web.dto.response.AuthenticationResponse;
import com.e_commerce_creator.web.response.AppResponse;
import com.e_commerce_creator.web.service.user.AuthenticationService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    final AuthenticationService authenticationService;


    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponse<JsonNode>> register(@RequestBody RegisterRequest request) {
        AppResponse.ResponseBuilder<JsonNode> responseBuilder = AppResponse.builder();
        try {
            AuthenticationResponse response = authenticationService.register(request);
            responseBuilder.data(SystemUtil.convertStringToJsonNode(response));
            responseBuilder.status(ResponseCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseBuilder.info("errorMessage", e.getMessage());
            responseBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build().getResponseEntity();
    }

    @PostMapping("authenticate")
    public ResponseEntity<AppResponse<JsonNode>> authenticate(@RequestBody AuthenticationRequest request) {
        AppResponse.ResponseBuilder<JsonNode> responseBuilder = AppResponse.builder();
        try {
            AuthenticationResponse response = authenticationService.authenticate(request);
            responseBuilder.data(SystemUtil.convertStringToJsonNode(response));
            responseBuilder.status(ResponseCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            responseBuilder.info("errorMessage", e.getMessage());
            responseBuilder.status(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build().getResponseEntity();
    }

}
