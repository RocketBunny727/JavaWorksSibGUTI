package org.example.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Embedded
    private Weight shippingWeight;
}
