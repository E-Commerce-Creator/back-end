package com.e_commerce_creator.web.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AuthenticationRequest {
    String username;
    String email;
    String password;

}
