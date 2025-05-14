package com.example.searchservice.core.repository;

import com.example.searchservice.core.model.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICatalogRepository extends JpaRepository<Catalog, Long> {
    boolean existsByName(String name);
}
