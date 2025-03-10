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
    PetsTagsId petsTagsId;

    @ManyToOne
    @JoinColumn(name = "pet_id", referencedColumnName = "id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "id", nullable = false)
    private Tag tag;
}