package com.example.service;

import com.example.model.Pet;
import com.example.repository.DB_PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DB_SupportService {

    private final DB_PetRepository petRepository;

    public DB_SupportService(DB_PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Optional<Pet> getPetById(Long id) {
        return petRepository.findById(id);
    }

    public Pet savePet(Pet pet) {
        return petRepository.save(pet);
    }

    public Pet updatePet(Pet pet) {
        return petRepository.save(pet);
    }

    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }
}
