package org.example.service;

import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.dto.SignupResponse;
import org.example.model.AuthUser;
import org.example.model.Customer;
import org.example.model.Token;
import org.example.repository.IAuthUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final IAuthUserRepository authUserRepository;
    private final JwtTokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;

    public AuthService(IAuthUserRepository authUserRepository,
                       JwtTokenService tokenService,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       CustomerService customerService) {
        this.authUserRepository = authUserRepository;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.customerService = customerService;
    }

    public SignupResponse signup(AuthRequest authRequest) {
        if (authUserRepository.findByLogin(authRequest.getLogin()).isPresent()) {
            return new SignupResponse("User already exists");
        }

        AuthUser newUser = AuthUser.builder()
                .login(authRequest.getLogin())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .userType(authRequest.getUserType())
                .customer(authRequest.getCustomer())
                .build();

        addAuthUser(newUser);

        return new SignupResponse("User created successfully");
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
        );

        AuthUser user = authUserRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Token token = tokenService.generateToken(user);
        return new AuthResponse(token.getToken());
    }

    public AuthUser addAuthUser(AuthUser authUser) {
        Customer customer = customerService.addNewCustomerIfNotExists(authUser.getCustomer());
        authUser.setCustomer(customer);
        return authUserRepository.save(authUser);
    }
}
