package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CustomerRequest;
import org.example.model.Customer;
import org.example.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<?> getAllCustomers(Authentication authentication) {
        boolean isAdmin = hasRole(authentication, "ADMIN");

        if (isAdmin) {
            return ResponseEntity.ok(customerService.findAll());
        } else {
            String login = authentication.getName();
            Optional<Customer> optionalCustomer = customerService.findCustomerByLogin(login);
            if (optionalCustomer.isPresent()) {
                return ResponseEntity.ok(List.of(optionalCustomer.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No orders found");
            }
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable long id, Authentication authentication) {
        Optional<Customer> customer = customerService.findCustomerById(id);
        if (customer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order with id " + id + " not found");
        }

        boolean isAdmin = hasRole(authentication, "ADMIN");
        boolean isOwner = customerService.findCustomerByLogin(authentication.getName())
                .map(c -> c.equals(customer.get()))
                .orElse(false);

        if (isAdmin || isOwner) {
            return ResponseEntity.ok(customer.get());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable long id, Authentication authentication, @RequestBody CustomerRequest body) {
        Optional<Customer> customer = customerService.findCustomerById(id);
        if (customer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order with id " + id + " not found");
        }

        boolean isAdmin = hasRole(authentication, "ADMIN");
        boolean isOwner = customerService.findCustomerByLogin(authentication.getName())
                .map(c -> c.equals(customer.get()))
                .orElse(false);

        if (isAdmin || isOwner) {
            Customer updatedCustomer = customerService.updateCustomer(id, body);
            return ResponseEntity.ok(updatedCustomer);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable long id, Authentication authentication) {
        Optional<Customer> customer = customerService.findCustomerById(id);
        if (customer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order with id " + id + " not found");
        }

        boolean isAdmin = hasRole(authentication, "ADMIN");
        boolean isOwner = customerService.findCustomerByLogin(authentication.getName())
                .map(c -> c.equals(customer.get()))
                .orElse(false);

        if (isAdmin || isOwner) {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }

    private boolean hasRole(Authentication auth, String role) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

}
