package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.model.Pet;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DB_PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByStatus(String status);

    List<Pet> findByNameContainingIgnoreCase(String name);
}