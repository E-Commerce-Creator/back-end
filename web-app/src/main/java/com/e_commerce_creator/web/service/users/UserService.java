package com.e_commerce_creator.web.service.users;

import com.e_commerce_creator.common.enums.response.ResponseCode;
import com.e_commerce_creator.common.enums.user.Permission;
import com.e_commerce_creator.common.enums.user.Role;
import com.e_commerce_creator.common.exception.ECCException;
import com.e_commerce_creator.common.model.account.Account;
import com.e_commerce_creator.common.model.users.User;
import com.e_commerce_creator.common.repository.account.AccountRepository;
import com.e_commerce_creator.common.repository.users.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    final UserRepository userRepository;
    final AccountRepository accountRepository;


    //CRUD user with specific role OWNER, ADMIN, USER

    public ObjectNode getUserDetails(Account account) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        User user = userRepository.findByUsername(account.getUsername()).orElseThrow(() -> new EntityNotFoundException("User not found with username " + account.getUsername()));

        ObjectNode basicDetail = mapper.createObjectNode();
        basicDetail.put("id", user.getId());
        basicDetail.put("username", user.getUsername());
        basicDetail.put("email", user.getEmail());
        basicDetail.put("fullName", user.getFullName());
        basicDetail.put("nationalId", user.getNationalId());
        ObjectNode roleNode = mapper.createObjectNode();
        Role role = user.getAccount().getRole();
        roleNode.putPOJO("role", role);
        roleNode.putPOJO("permissions", role.getPermissions());
        basicDetail.putPOJO("role", roleNode);

        node.putPOJO("details", basicDetail);
        return node;
    }

    public void addOrRemovePermission(Long id, Permission newPermission, boolean add) {
        userRepository.findById(id).ifPresentOrElse(user -> {
            Account account = user.getAccount();
            // Convert comma-separated permissions into a HashSet (O(N) once)
            Set<String> permissionSet = new HashSet<>(Arrays.asList(account.getPermissions().split(",")));
            if (add ? permissionSet.add(newPermission.getOperation()) : permissionSet.remove(newPermission.getOperation())) {
                String permission = String.join(",", permissionSet);
                account.setPermissions(permission);
                accountRepository.save(account);
            }
        }, () -> new ECCException(ResponseCode.NOT_EXIST));
    }

    public void changeRole(Long id, Role role) {
        userRepository.findById(id).ifPresentOrElse(user -> {
            Account account = user.getAccount();
            account.setRole(role);
            String permissions = role.getPermissions().stream().map(Permission::getOperation).collect(Collectors.joining(","));
            account.setPermissions(permissions);
            accountRepository.save(account);
        }, () -> new ECCException(ResponseCode.NOT_EXIST));
    }
}
