package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.OrderRequest;
import org.example.dto.SearchRequest;
import org.example.model.Customer;
import org.example.model.Order;
import org.example.service.CustomerService;
import org.example.service.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(Authentication authentication) {
        if (hasRole(authentication, "ADMIN")) {
            return ResponseEntity.ok(service.findAll());
        }

        Customer customer = customerService.findCustomerByLogin(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User with login " + authentication.getName() + " not found"));

        return ResponseEntity.ok(service.findOrdersByCustomer(customer));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Order>> getOrdersByFilter(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String street,
            @RequestParam(required = false) String number,
            @RequestParam(required = false) String zipcode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) String paymentType,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String orderStatus,
            Authentication authentication) {

        if (!hasRole(authentication, "ADMIN")) {
            customerName = authentication.getName();
        }

        List<Order> orders = service.findOrders(city, street, number, zipcode, from, to, paymentType, customerName, orderStatus);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Order>> getOrdersByFilter(@RequestBody SearchRequest body, Authentication authentication) {
        if (!hasRole(authentication, "ADMIN")) {
            body.setCustomerName(authentication.getName());
        }

        List<Order> orders = service.findOrders(
                body.getCity(), body.getStreet(), body.getNumber(), body.getZipcode(),
                body.getFrom(), body.getTo(), body.getPaymentType(), body.getCustomerName(), body.getOrderStatus()
        );
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<?> addOrder(@RequestBody OrderRequest body, Authentication authentication) {
        String login = authentication.getName();

        boolean isRegular = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_REGULAR"));

        if (isRegular) {
            Optional<Customer> optionalCustomer = customerService.findCustomerByLogin(login);
            if (optionalCustomer.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found for login: " + login);
            }
            body.setCustomer(optionalCustomer.get());
        }

        try {
            Order order = service.createOrder(body);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid request: " + e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable long id, Authentication authentication) {
        Optional<Order> optionalOrder = service.findById(id);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Order order = optionalOrder.get();
        boolean isAdmin = hasRole(authentication, "ADMIN");
        boolean isOwner = customerService.findCustomerByLogin(authentication.getName())
                .map(customer -> customer.equals(order.getCustomer()))
                .orElse(false);

        if (isAdmin || isOwner) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable long id, @RequestBody OrderRequest body, Authentication authentication) {
        Optional<Order> optionalOrder = service.findById(id);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order with id " + id + " not found");
        }

        Order order = optionalOrder.get();
        boolean isAdmin = hasRole(authentication, "ADMIN");
        boolean isOwner = customerService.findCustomerByLogin(authentication.getName())
                .map(customer -> customer.equals(order.getCustomer()))
                .orElse(false);

        if (isAdmin || isOwner) {
            Order updatedOrder = service.updateOrder(id, body);
            return ResponseEntity.ok(updatedOrder);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable long id, Authentication authentication) {
        Optional<Order> optionalOrder = service.findById(id);
        if (optionalOrder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order with id " + id + " not found");
        }

        Order order = optionalOrder.get();
        boolean isAdmin = hasRole(authentication, "ADMIN");
        boolean isOwner = customerService.findCustomerByLogin(authentication.getName())
                .map(customer -> customer.equals(order.getCustomer()))
                .orElse(false);

        if (isAdmin || isOwner) {
            service.deleteOrder(id);
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
