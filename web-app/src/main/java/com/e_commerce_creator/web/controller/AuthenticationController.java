package com.e_commerce_creator.web.controller;

import com.e_commerce_creator.common.enums.response.ResponseCode;
import com.e_commerce_creator.common.util.SystemUtil;
import com.e_commerce_creator.web.dto.request.AuthenticationRequest;
import com.e_commerce_creator.web.dto.request.RegisterRequest;
import com.e_commerce_creator.web.response.AppResponse;
import com.e_commerce_creator.web.service.users.AuthenticationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("api/v1/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    final AuthenticationService authenticationService;


    @PostMapping(value = "/register")
    public ResponseEntity<AppResponse<JsonNode>> register(@RequestBody RegisterRequest request) {
        AppResponse.ResponseBuilder<JsonNode> responseBuilder = AppResponse.builder();
        try {
            String token = authenticationService.register(request);
            ObjectMapper mapper = new ObjectMapper();

            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("token", token);
            responseBuilder.data(SystemUtil.convertStringToJsonNode(objectNode));
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
            String token = authenticationService.authenticate(request);
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("token", token);
            responseBuilder.data(SystemUtil.convertStringToJsonNode(objectNode));
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
