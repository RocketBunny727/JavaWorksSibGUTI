package com.example.searchservice.api.controller;

import com.example.searchservice.api.dto.CatalogDto;
import com.example.searchservice.api.dto.ItemDto;
import com.example.searchservice.api.dto.ProductDto;
import com.example.searchservice.core.model.Catalog;
import com.example.searchservice.core.model.Product;
import com.example.searchservice.core.service.InsertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/insert")
@RequiredArgsConstructor
public class InsertController {

    private final InsertService insertService;

    @PostMapping("/catalog")
    public ResponseEntity<Catalog> insertCatalog(@RequestBody CatalogDto catalogDto) {
        Catalog catalog = insertService.insertCatalog(catalogDto);
        return ResponseEntity.ok(catalog);
    }

    @PostMapping("/product")
    public ResponseEntity<Product> insertProduct(@RequestBody ProductDto productDto) {
        Product product = insertService.insertProduct(productDto);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/catalog/{id}")
    public ResponseEntity<Catalog> updateCatalog(@PathVariable Long id, @RequestBody CatalogDto catalogDto) {
        Catalog catalog = insertService.updateCatalog(id, catalogDto);
        return ResponseEntity.ok(catalog);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Product product = insertService.updateProduct(id, productDto);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/catalog/{id}")
    public ResponseEntity<Void> deleteCatalog(@PathVariable Long id) {
        insertService.deleteCatalog(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        insertService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
