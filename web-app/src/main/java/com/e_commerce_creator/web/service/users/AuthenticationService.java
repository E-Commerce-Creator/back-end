package com.e_commerce_creator.web.service.users;

import com.e_commerce_creator.common.enums.account.CityCode;
import com.e_commerce_creator.common.enums.account.Gender;
import com.e_commerce_creator.common.model.account.Account;
import com.e_commerce_creator.common.repository.account.AccountRepository;
import com.e_commerce_creator.common.util.TimeUtils;
import com.e_commerce_creator.web.config.security.TokenService;
import com.e_commerce_creator.web.dto.request.AuthenticationRequest;
import com.e_commerce_creator.web.dto.request.RegisterRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    static final String EMAIL_REGEX = "^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    static final List<String> SUPPORTED_PROVIDERS = List.of("gmail.com", "outlook.com", "yahoo.com");
    final AccountRepository accountRepository;
    final PasswordEncoder passwordEncoder;
    final TokenService tokenService;
    final AuthenticationManager authenticationManager;

    public String register(RegisterRequest registerRequest) {
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

        Account savedAccount = accountRepository.save(account);

        return savedAccount.getId().toString();
    }

    @Transactional
    public String authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsernameOrEmail(), authenticationRequest.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Account account;
        if (Pattern.matches(EMAIL_REGEX, authenticationRequest.getUsernameOrEmail()))
            account = accountRepository.findAccountByEmail(authenticationRequest.getUsernameOrEmail()).orElseThrow(() -> new EntityNotFoundException("account not found"));
        else
            account = accountRepository.findAccountByUsername(authenticationRequest.getUsernameOrEmail()).orElseThrow(() -> new EntityNotFoundException("account not found"));
        String token = tokenService.generateToken(account);
        return token;
    }
}