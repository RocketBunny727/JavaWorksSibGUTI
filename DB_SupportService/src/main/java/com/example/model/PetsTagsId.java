package com.example.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PetsTagsId implements Serializable {

    private Long petId;
    private Long tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetsTagsId that = (PetsTagsId) o;
        return Objects.equals(petId, that.petId) && Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(petId, tagId);
    }
}
