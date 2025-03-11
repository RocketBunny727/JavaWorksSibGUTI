package com.example.controller;

import com.example.model.Pet;
import com.example.service.DB_SupportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pets")
public class DB_SupportController {

    private final DB_SupportService service;

    public DB_SupportController(DB_SupportService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Pet> createPet(@RequestBody Pet pet) {
        Pet newPet = service.createPet(pet);
        return ResponseEntity.ok(newPet);
    }

    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        List<Pet> pets = service.getAllPets();
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/{pet_id}")
    public ResponseEntity<Pet> getPetById(@PathVariable long pet_id) {
        Optional<Pet> pet = service.getPetById(pet_id);
        return pet.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{pet_id}")
    public ResponseEntity<Pet> updatePet(@PathVariable long pet_id, @RequestBody Pet pet) {
        try {
            Pet updatedPet = service.updatePet(pet_id, pet);
            return ResponseEntity.ok(updatedPet);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{pet_id}")
    public ResponseEntity<Pet> deletePet(@PathVariable long pet_id) {
        service.deletePet(pet_id);
        return ResponseEntity.noContent().build();
    }
}
