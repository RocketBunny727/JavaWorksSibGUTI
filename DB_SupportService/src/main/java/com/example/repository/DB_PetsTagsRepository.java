package com.example.repository;

import com.example.model.Pet;
import com.example.model.PetsTags;
import com.example.model.PetsTagsId;
import com.example.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DB_PetsTagsRepository extends JpaRepository<PetsTags, PetsTagsId> {

    List<PetsTags> findByTag(Tag tag);
}
