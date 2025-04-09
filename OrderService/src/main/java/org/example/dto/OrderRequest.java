package org.example.dto;

import lombok.*;
import org.example.model.Customer;
import org.example.model.OrderDetail;
import org.example.model.Payment;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private LocalDateTime date;

    private String status;

    private Customer customer;

    private List<OrderDetail> orderDetails;

    private List<Payment> payments;
}
