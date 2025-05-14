package com.example.searchservice.core.service;

import com.example.searchservice.api.dto.CatalogProductDto;
import com.example.searchservice.core.cache.SearchCache;
import com.example.searchservice.core.model.Catalog;
import com.example.searchservice.core.model.Product;
import com.example.searchservice.core.repository.ICatalogRepository;
import com.example.searchservice.core.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ICatalogRepository catalogRepository;
    private final IProductRepository productRepository;
    private final SearchCache searchCache;

    private static final int SUGGESTION_LIMIT = 5;

    public CatalogProductDto search(String query) {
        String normalizedQuery = query.toLowerCase();

        // Префиксные подсказки по кэшу
        List<String> productSuggestions = searchCache.prefixSearch(
                searchCache.getProductMap(), normalizedQuery, SUGGESTION_LIMIT);
        List<String> catalogSuggestions = searchCache.prefixSearch(
                searchCache.getCatalogMap(), normalizedQuery, SUGGESTION_LIMIT);
        List<String> itemSuggestions = searchCache.prefixSearch(
                searchCache.getItemMap(), normalizedQuery, SUGGESTION_LIMIT);

        Set<String> suggestions = new LinkedHashSet<>();
        suggestions.addAll(catalogSuggestions);
        suggestions.addAll(productSuggestions);
        suggestions.addAll(itemSuggestions);

        // Найти по точному совпадению части имени
        List<Long> productIds = productSuggestions.stream()
                .map(name -> searchCache.getProductMap().get(name))
                .filter(Objects::nonNull)
                .toList();
        List<Long> catalogIds = catalogSuggestions.stream()
                .map(name -> searchCache.getCatalogMap().get(name))
                .filter(Objects::nonNull)
                .toList();
        List<Long> itemCatalogIds = itemSuggestions.stream()
                .map(name -> searchCache.getItemMap().get(name))
                .filter(Objects::nonNull)
                .toList();

        List<Product> products = productRepository.findAllById(productIds);
        List<Catalog> catalogs = catalogRepository.findAllById(
                Stream.concat(catalogIds.stream(), itemCatalogIds.stream())
                        .distinct()
                        .toList()
        );

        return CatalogProductDto.builder()
                .catalogs(catalogs)
                .products(products)
                .suggestions(new ArrayList<>(suggestions))
                .build();
    }

    public Optional<Catalog> findCatalogById(Long id) {
        return catalogRepository.findById(id);
    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }
}

