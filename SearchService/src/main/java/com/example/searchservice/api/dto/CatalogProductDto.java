package com.example.searchservice.api.dto;

import com.example.searchservice.core.model.Catalog;
import com.example.searchservice.core.model.Product;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CatalogProductDto {
    private List<Catalog> catalogs;
    private List<Product> products;
    private List<String> suggestions;
}
