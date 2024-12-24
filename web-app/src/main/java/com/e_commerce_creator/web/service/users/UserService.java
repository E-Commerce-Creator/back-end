package com.e_commerce_creator.web.service.users;

import com.e_commerce_creator.common.model.users.User;
import com.e_commerce_creator.common.repository.users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    final UserRepository userRepository;


    //CRUD user with specific role OWNER, ADMIN, USER

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found :: " + id));
    }
}
