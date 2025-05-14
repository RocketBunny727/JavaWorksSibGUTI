package com.example;

import com.example.model.*;
import com.example.repository.DB_CategoryRepository;
import com.example.repository.DB_PetRepository;
import com.example.repository.DB_TagRepository;
import com.example.service.DB_SupportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DB_SupportServiceTest {

    @Mock
    private DB_PetRepository petRepository;

    @Mock
    private DB_CategoryRepository categoryRepository;

    @Mock
    private DB_TagRepository tagRepository;

    @InjectMocks
    private DB_SupportService service;

    private Pet validPet;
    private Category existingCategory;
    private Tag existingTag;

    @BeforeEach
    public void setUp() {
        existingCategory = new Category(1L, "Dog");
        existingTag = new Tag(1L, "Hunter");
        validPet = new Pet(1L, "Emily", null, null, Status.AVAILABLE);
    }

    @Test
    void addPet_shouldSaveNewCategoryAndTags() {
        Category newCategory = new Category(null, "Reptiles");
        Tag newTag = new Tag(null, "Scaly");
        validPet.setCategory(newCategory);
        validPet.setTags(List.of(newTag));

        when(categoryRepository.findByName("Reptiles")).thenReturn(Optional.empty());
        when(categoryRepository.save(newCategory)).thenReturn(new Category(3L, "Reptiles"));
        when(tagRepository.findByName("Scaly")).thenReturn(Optional.empty());
        when(tagRepository.save(newTag)).thenReturn(new Tag(3L, "Scaly"));
        when(petRepository.save(validPet)).thenReturn(validPet);

        Pet result = service.createPet(validPet);

        assertNotNull(result);
        assertEquals(3L, result.getCategory().getId());
        assertEquals(3L, result.getTags().get(0).getId());
    }

    @Test
    void addPet_shouldUseExistingCategoryAndTags() {
        validPet.setCategory(existingCategory);
        validPet.setTags(List.of(existingTag));

        when(categoryRepository.findByName("Dog")).thenReturn(Optional.of(existingCategory));
        when(tagRepository.findByName("Hunter")).thenReturn(Optional.of(existingTag));
        when(petRepository.save(validPet)).thenReturn(validPet);

        Pet result = service.createPet(validPet);

        assertEquals(1L, result.getCategory().getId());
        assertEquals(1L, result.getTags().get(0).getId());
    }

    @Test
    void getPetById_shouldReturnPetWhenExists() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(validPet));

        Optional<Pet> result = service.getPetById(1L);

        assertNotNull(result);
        assertEquals(1L, result.get().getId());
    }

    @Test
    void deletePet_shouldCallRepositoryDelete() {
        doNothing().when(petRepository).deleteById(1L);

        service.deletePet(1L);

        verify(petRepository).deleteById(1L);
    }

    @Test
    void updatePet_shouldMergeExistingAndNewTags() {
        validPet.setTags(List.of(existingTag, new Tag(null, "NewTag")));
        when(petRepository.findById(1L)).thenReturn(Optional.of(validPet));
        when(tagRepository.findByName("Hunter")).thenReturn(Optional.of(existingTag));
        when(tagRepository.findByName("NewTag")).thenReturn(Optional.empty());
        when(tagRepository.save(any())).thenReturn(new Tag(2L, "NewTag"));
        when(petRepository.save(validPet)).thenReturn(validPet);

        Pet result = service.updatePet(validPet);

        assertEquals(2, result.getTags().size());
        assertEquals("NewTag", result.getTags().get(1).getName());
    }
}
