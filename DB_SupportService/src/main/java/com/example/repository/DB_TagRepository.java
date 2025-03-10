package com.example.repository;

import com.example.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DB_TagRepository extends JpaRepository<Tag, Long> {
}