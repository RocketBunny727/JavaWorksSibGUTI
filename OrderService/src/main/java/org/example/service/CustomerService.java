package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.dto.CustomerRequest;
import org.example.dto.SignupResponse;
import org.example.model.Address;
import org.example.model.AuthUser;
import org.example.model.Customer;
import org.example.model.Token;
import org.example.repository.IAddressRepository;
import org.example.repository.IAuthUserRepository;
import org.example.repository.ICustomerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final ICustomerRepository customerRepository;
    private final IAuthUserRepository authUserRepository;
    private final IAddressRepository addressRepository;
    private final JwtTokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

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
        Customer customer = addNewCustomerIfNotExists(authUser.getCustomer());
        authUser.setCustomer(customer);
        return authUserRepository.save(authUser);
    }

    public Optional<Customer> findCustomerByLogin(String login) {
        return authUserRepository.findByLogin(login)
                .map(AuthUser::getCustomer);
    }

    public Optional<Customer> findCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    private Customer addNewCustomerIfNotExists(Customer customer) {
        Optional<Customer> existingCustomer = customerRepository.findByName(customer.getName());

        if (existingCustomer.isPresent()) {
            return existingCustomer.get();
        }

        Address address = customer.getAddress();
        Optional<Address> existingAddress = addressRepository.findByCityAndStreetAndNumberAndZipcode(
                address.getCity(),
                address.getStreet(),
                address.getNumber(),
                address.getZipcode()
        );

        if (existingAddress.isPresent()) {
            customer.setAddress(existingAddress.get());
        } else {
            Address savedAddress = addressRepository.saveAndFlush(address);
            customer.setAddress(savedAddress);
        }

        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, CustomerRequest body) {
        return customerRepository.findById(id).map( customer -> {
            customer.setName(body.getName());

            Address address = addressRepository.findByCityAndStreetAndNumberAndZipcode(
                    body.getAddress().getCity(),
                    body.getAddress().getStreet(),
                    body.getAddress().getNumber(),
                    body.getAddress().getZipcode()
            ).orElseGet(() -> addressRepository.save(body.getAddress()));

            customer.setAddress(address);

            return customerRepository.save(customer);
        }).orElseThrow(() -> new EntityNotFoundException("Customer with id " + id + " not found"));
    }

    public void deleteCustomer(Long id) { customerRepository.deleteById(id); }
}
