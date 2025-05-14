package org.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "payment_check")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Check extends Payment {

    @Column(nullable = false)
    private String name;

    @Column(name = "bank_id", nullable = false)
    private String bankId;
}
