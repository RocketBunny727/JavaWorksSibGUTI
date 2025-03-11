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

    public Pet createPet(Pet pet) {
        return petRepository.save(pet);
    }

    public Optional<Pet> getPetById(Long id) {
        return petRepository.findById(id);
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Pet updatePet(Long id, Pet updatedPet) {
        return petRepository.findById(id)
                .map(existingPet -> {
                    existingPet.setName(updatedPet.getName());
                    existingPet.setCategory(updatedPet.getCategory());
                    existingPet.setTags(updatedPet.getTags());
                    existingPet.setStatus(updatedPet.getStatus());
                    return petRepository.save(existingPet);
                })
                .orElseThrow(() -> new RuntimeException("Питомец с ID " + id + " не найден"));
    }

    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }
}
