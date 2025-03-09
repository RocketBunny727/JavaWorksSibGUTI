package com.example.demo.repository;

import com.example.demo.model.Pet;

import java.util.Optional;

public interface IPetRepository {

    Pet addPet(Pet pet);

    Pet updatePet(Pet pet);

    Optional<Pet> findById(int petId);

    void deletePetById(int petId);

}
