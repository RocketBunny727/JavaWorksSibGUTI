package com.example.repository;

import com.example.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DB_TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}