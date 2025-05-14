package org.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_credit")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Credit extends Payment {

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String type;

    @Column(name = "exp_date", nullable = false)
    private LocalDateTime expDate;
}
