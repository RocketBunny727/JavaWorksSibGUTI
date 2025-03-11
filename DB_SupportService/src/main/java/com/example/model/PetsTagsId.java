package com.example.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PetsTagsId implements Serializable {

    private Long pet_id;
    private Long tag_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetsTagsId that = (PetsTagsId) o;
        return Objects.equals(pet_id, that.pet_id) && Objects.equals(tag_id, that.tag_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pet_id, tag_id);
    }
}
