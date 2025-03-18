package com.example;

import com.example.controller.DB_SupportController;
import com.example.model.Category;
import com.example.model.Pet;
import com.example.model.Status;
import com.example.service.DB_SupportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DB_SupportController.class)
@ExtendWith(MockitoExtension.class)
public class DB_SupportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DB_SupportService service;

    @Autowired
    private ObjectMapper mapper;

    private Pet pet;

    @BeforeEach
    void setUp() {
        pet = new Pet(1L, "Jack", new Category(1L, "Dogs"), null, Status.AVAILABLE);
    }

    @Test
    void shouldCreatePet() throws Exception {
        when(service.createPet(any(Pet.class))).thenReturn(pet);

        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(pet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Jack"))
                .andExpect(jsonPath("$.status").value(Status.AVAILABLE.toString()));
    }

    @Test
    void shouldUpdatePet() throws Exception {
        Pet updatedPet = new Pet(1L, "Bob", new Category(1L, "Dogs"), null, Status.AVAILABLE);
        when(service.updatePet(any(Pet.class))).thenReturn(updatedPet);

        mockMvc.perform(put("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedPet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.status").value(Status.AVAILABLE.toString()));
    }

    @Test
    void shouldGetPet() throws Exception {
        when(service.getPetById(1L)).thenReturn(Optional.ofNullable(pet));

        mockMvc.perform(get("/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(pet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Jack"))
                .andExpect(jsonPath("$.status").value(Status.AVAILABLE.toString()));
    }

    @Test
    void shouldDeletePet() throws Exception {
        doNothing().when(service).deletePet(1L);

        mockMvc.perform(delete("/pets/1"))
                .andExpect(status().isNoContent());

        verify(service).deletePet(1L);
    }
}
