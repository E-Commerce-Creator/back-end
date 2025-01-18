package com.e_commerce_creator.web.service.users;

import com.e_commerce_creator.common.model.account.Account;
import com.e_commerce_creator.common.model.users.User;
import com.e_commerce_creator.common.repository.users.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    final UserRepository userRepository;


    //CRUD user with specific role OWNER, ADMIN, USER

    public ObjectNode getUserDetails(Account account) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        User user = userRepository.findByUsername(account.getUsername()).orElseThrow(() -> new EntityNotFoundException("User not found with username " + account.getUsername()));

        ObjectNode basicDetail = mapper.createObjectNode();
        basicDetail.put("username", user.getUsername());
        basicDetail.put("email", user.getEmail());
        basicDetail.put("fullName", user.getFullName());
        basicDetail.put("nationalId", user.getNationalId());
        ObjectNode roleNode = mapper.createObjectNode();
        roleNode.putPOJO("role", user.getAccount().getRole());
        roleNode.putPOJO("permission", Set.of(user.getAccount().getRole().getPermissions()));
        basicDetail.putPOJO("role", roleNode);

        node.putPOJO("details", basicDetail);
        return node;
    }
}
