package com.e_commerce_creator.web.dto.request;

import com.e_commerce_creator.common.enums.user.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class RegisterRequest {
    String firstName;
    String lastName;
    String email;
    String username;
    String password;
    String nationalId;
    Role role;
}
