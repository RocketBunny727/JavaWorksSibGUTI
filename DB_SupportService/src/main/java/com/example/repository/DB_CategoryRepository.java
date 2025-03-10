package com.example.repository;

import com.example.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DB_CategoryRepository extends JpaRepository<Category, Long> {
}
