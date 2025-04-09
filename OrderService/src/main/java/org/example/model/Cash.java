package org.example.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_cash")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cash extends Payment {

    @Column(name = "cash_tendered", nullable = false)
    private float cashTendered;
}
