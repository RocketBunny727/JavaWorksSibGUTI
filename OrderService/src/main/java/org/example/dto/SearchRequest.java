package org.example.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    private String city;
    private String street;
    private String number;
    private String zipcode;

    private LocalDateTime from;
    private LocalDateTime to;

    private String paymentType;

    private String customerName;

    private String orderStatus;
}
