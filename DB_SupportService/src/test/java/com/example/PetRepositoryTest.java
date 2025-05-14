package com.example;

import com.example.model.*;
import com.example.repository.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class PetRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private DB_CategoryRepository categoryRepository;

    @Autowired
    private DB_TagRepository tagRepository;

    @Autowired
    private DB_PetRepository petRepository;

    @BeforeAll
    static void setup() {
        postgres.start();
    }

    @AfterAll
    static void cleanup() {
        postgres.stop();
    }

    @Test
    void testCategoryRepositoryFindByName() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Dogs");
        categoryRepository.save(category);

        Optional<Category> foundCategory = categoryRepository.findByName("Dogs");
        assertTrue(foundCategory.isPresent());
        assertEquals("Dogs", foundCategory.get().getName());
    }

    @Test
    void testTagRepositoryFindByName() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Hunter");
        tagRepository.save(tag);

        Optional<Tag> foundTag = tagRepository.findByName(tag.getName());
        assertTrue(foundTag.isPresent());
        assertEquals("Hunter", foundTag.get().getName());
    }

    @Test
    void testPetRepositorySaveAndFind() {
        Category category = new Category();
        category.setName("Dogs");
        categoryRepository.save(category);
        Tag tag = new Tag();
        tag.setName("Small");
        tagRepository.save(tag);

        Pet pet = new Pet();
        pet.setName("Bobik");
        pet.setCategory(category);
        pet.setTags(List.of(tag));

        petRepository.save(pet);
        Optional<Pet> foundPet = petRepository.findById(pet.getId());

        assertTrue(foundPet.isPresent());
        assertEquals(pet.getName(), foundPet.get().getName());
        assertEquals(category.getName(), foundPet.get().getCategory().getName());
        assertEquals(pet.getTags().size(), foundPet.get().getTags().size());
        assertEquals(tag.getName(), foundPet.get().getTags().get(0).getName());
    }
}
