package com.e_commerce_creator.web.service.users;

import com.e_commerce_creator.common.enums.account.CityCode;
import com.e_commerce_creator.common.enums.account.Gender;
import com.e_commerce_creator.common.enums.response.ResponseCode;
import com.e_commerce_creator.common.exception.ECCException;
import com.e_commerce_creator.common.model.account.Account;
import com.e_commerce_creator.common.model.users.User;
import com.e_commerce_creator.common.repository.account.AccountRepository;
import com.e_commerce_creator.common.repository.users.UserRepository;
import com.e_commerce_creator.common.util.SystemUtils;
import com.e_commerce_creator.common.util.TimeUtils;
import com.e_commerce_creator.web.config.security.TokenService;
import com.e_commerce_creator.web.dto.request.AuthenticationRequest;
import com.e_commerce_creator.web.dto.request.RegisterRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    static final String EMAIL_REGEX = "^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    static final List<String> SUPPORTED_PROVIDERS = List.of("gmail.com", "outlook.com", "yahoo.com");
    final AccountRepository accountRepository;
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final TokenService tokenService;
    final AuthenticationManager authenticationManager;

    public String register(RegisterRequest registerRequest) throws ECCException {
        Optional<String> conflictField = userRepository.findConflictField(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getNationalId());
        if (conflictField.isPresent()) {
            throw new ECCException(conflictField.get() + "_" + ResponseCode.ALREADY_EXIST.getMessage(), ResponseCode.ALREADY_EXIST);
        }

        String nationalId = registerRequest.getNationalId();
        int year = Integer.parseInt((nationalId.charAt(0) == '2' ? "19" : "20") + nationalId.substring(1, 3));
        int month = Integer.parseInt(nationalId.substring(3, 5));
        int day = Integer.parseInt(nationalId.substring(5, 7));
        int age = TimeUtils.calculateAge(LocalDate.of(year, month, day));
        String city = CityCode.getCityNameByCode(nationalId.substring(7, 9));
        Gender gender = Integer.parseInt(nationalId.substring(9, 13)) % 2 == 0 ? Gender.FEMALE : Gender.MALE;

        Account account = Account.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .nationalId(nationalId)
                .age(age)
                .city(city)
                .gender(gender)
                .role(registerRequest.getRole())
                .build();

        User user = User.builder()
                .fullName(registerRequest.getFirstName() + " " + registerRequest.getLastName())
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .nationalId(registerRequest.getNationalId())
                .account(account)
                .build();

        account.setUser(user);//cascade persist account also with user

        User savedUser = userRepository.save(user);

        return savedUser.getId().toString();
    }

    @Transactional
    public ObjectNode authenticate(AuthenticationRequest authenticationRequest) throws ECCException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsernameOrEmail(), authenticationRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new ECCException(ResponseCode.INVALID_AUTH.getMessage(), ResponseCode.INVALID_AUTH);
        }

        Account account;
        if (Pattern.matches(EMAIL_REGEX, authenticationRequest.getUsernameOrEmail()))
            account = accountRepository.findAccountByEmail(authenticationRequest.getUsernameOrEmail()).orElseThrow(() -> new ECCException("account not found", ResponseCode.NOT_FOUND));
        else
            account = accountRepository.findAccountByUsername(authenticationRequest.getUsernameOrEmail()).orElseThrow(() -> new ECCException("account not found", ResponseCode.NOT_FOUND));
        String token = tokenService.generateToken(account);
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("token", token);
        objectNode.put("username", account.getUsername());
        return objectNode;
    }
}