package com.example.searchservice.api.controller;

import com.example.searchservice.api.dto.CatalogProductDto;
import com.example.searchservice.core.model.Catalog;
import com.example.searchservice.core.model.Product;
import com.example.searchservice.core.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<CatalogProductDto> searchByDebounce(@RequestParam String query) {
        log.info("[SearchController] Received search request for query: '{}'", query);
        return ResponseEntity.ok(searchService.search(query));
    }

    @GetMapping("/catalog/{id}")
    public ResponseEntity<Catalog> searchCatalog(@PathVariable Long id) {
        return searchService.findCatalogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> searchProduct(@PathVariable Long id) {
        return searchService.findProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

