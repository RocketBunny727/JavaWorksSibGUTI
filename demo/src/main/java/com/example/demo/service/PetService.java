package com.example.demo.service;

import com.example.demo.controller.InvalidInputException;
import com.example.demo.controller.PetNotFoundException;
import com.example.demo.controller.ValidationException;
import com.example.demo.model.Pet;
import com.example.demo.repository.IPetRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Service
public class PetService {

    private final IPetRepository petRepository;

    public PetService(IPetRepository petRepository) { this.petRepository = petRepository; }

    public Pet addPet(Pet pet) {
        validatePet(pet);
        return petRepository.addPet(pet);
    }

    public Pet updatePet(Pet pet) {
        validatePet(pet);
        petRepository.findById(pet.id()).orElseThrow(() -> new PetNotFoundException("Pet not found with ID: " + pet.id()));
        return petRepository.updatePet(pet);
    }

    public Optional<Pet> getPetById(String petId) {
        int id = validateAndParsePetId(petId);
        return Optional.of(petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Pet not found with ID: " + id)));
    }

    public void deletePet(String petId) {
        petRepository.deletePetById(validateAndParsePetId(petId));
    }

    private void validatePet(Pet pet) {
        Objects.requireNonNull(pet, "Pet cannot be null");
        if (!StringUtils.hasText(pet.name())) {
            throw new ValidationException("Pet name cannot be empty");
        }
        Objects.requireNonNull(pet.category(), "Pet category cannot be null");
    }

    private int validateAndParsePetId(String petId) {
        try {
            return Integer.valueOf(petId);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid pet ID format: " + petId);
        }
    }
}
