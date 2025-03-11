package com.example.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pets_tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetsTags {

    @EmbeddedId
    private PetsTagsId petsTagsId;

    @ManyToOne
    @MapsId("pet_id")
    @JoinColumn(name = "pet_id", referencedColumnName = "id", nullable = false)
    private Pet pet;

    @ManyToOne
    @MapsId("tag_id")
    @JoinColumn(name = "tag_id", referencedColumnName = "id", nullable = false)
    private Tag tag;
}
