package org.example.dto;

import lombok.*;
import org.example.model.Customer;
import org.example.model.UserType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    private String login;
    private String password;
    private UserType userType;
    private Customer customer;
}
