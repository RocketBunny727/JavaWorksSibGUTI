package com.example.demo.repository;

import com.example.demo.model.Pet;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class PetRepositoryWithMap implements IPetRepository{

    private final Map<Integer, Pet> petStore = new HashMap<>();

    @Override
    public Pet addPet(Pet pet) {
        this.petStore.put(pet.id(), pet);
        return pet;
    }

    @Override
    public Pet updatePet(Pet pet) {
        return petStore.put(pet.id(), pet);
    }

    @Override
    public Optional<Pet> findById(int petId) {
        return Optional.ofNullable(petStore.get(petId));
    }

    @Override
    public void deletePetById(int petId) {
        petStore.remove(petId);
    }
}
