package com.example.service;

import com.example.model.Pet;
import com.example.model.Tag;
import com.example.repository.DB_CategoryRepository;
import com.example.repository.DB_PetRepository;
import com.example.repository.DB_TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DB_SupportService {

    private final DB_PetRepository petRepository;
    private final DB_CategoryRepository categoryRepository;
    private final DB_TagRepository tagRepository;

    public DB_SupportService(DB_PetRepository petRepository, DB_CategoryRepository categoryRepository, DB_TagRepository tagRepository) {
        this.petRepository = petRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    public Pet createPet(Pet pet) {

        if (pet.getCategory() != null) {
            categoryRepository.findByName(pet.getCategory().getName())
                    .ifPresentOrElse(
                            pet::setCategory,
                            () -> pet.setCategory(categoryRepository.save(pet.getCategory()))
                    );
        }

        List<Tag> tags = pet.getTags() == null ? new ArrayList<>() : new ArrayList<>(pet.getTags());
        if (!tags.isEmpty()) {
            tags.replaceAll(tag ->
                    tagRepository.findByName(tag.getName())
                            .orElseGet(() -> tagRepository.save(tag))
            );
        }
        pet.setTags(tags);

        return petRepository.save(pet);
    }

    public Optional<Pet> getPetById(Long id) {
        return petRepository.findById(id);
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Pet updatePet(Pet updatedPet) {
        petRepository.findById(updatedPet.getId())
                .orElseThrow(() -> new RuntimeException("Питомец с ID " + updatedPet.getId() + " не найден"));

        if (updatedPet.getCategory() != null) {
            updatedPet.setCategory(categoryRepository.findByName(updatedPet.getCategory().getName())
                    .orElseGet(() -> categoryRepository.save(updatedPet.getCategory())));
        }

        List<Tag> tags = updatedPet.getTags() == null ? new ArrayList<>() : new ArrayList<>(updatedPet.getTags());
        if (!tags.isEmpty()) {
            tags.replaceAll(tag -> tagRepository.findByName(tag.getName())
                    .orElseGet(() -> tagRepository.save(tag)));
        }
        updatedPet.setTags(tags);

        return petRepository.save(updatedPet);
    }

    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }
}
