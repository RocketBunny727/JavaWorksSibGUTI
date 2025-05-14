package com.example.searchservice.core.repository;

import com.example.searchservice.core.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    boolean existsByName(String name);
    Item findByName(String name);
}
