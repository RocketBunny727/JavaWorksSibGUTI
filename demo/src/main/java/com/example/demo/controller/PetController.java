package com.example.demo.controller;

import com.example.demo.model.Pet;
import com.example.demo.service.PetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) { this.petService = petService; }

    @PutMapping
    public ResponseEntity<Pet> updatePet(@RequestBody @Valid Pet pet) {
        return ResponseEntity.ok(petService.updatePet(pet));
    }

    @PostMapping
    public ResponseEntity<Pet> createPet(@RequestBody @Valid Pet pet) {
        return ResponseEntity.ok(petService.addPet(pet));
    }

    @GetMapping("/{petId}")
    public Optional<Pet> findPet(@Valid @PathVariable String petId) {
        return petService.getPetById(petId);
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(@Valid @PathVariable String petId) {
        petService.deletePet(petId);
        return ResponseEntity.ok().build();
    }
}

