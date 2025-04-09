package org.example.dto;

import lombok.*;
import org.example.model.Address;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {
    private String name;
    private Address address;
}
