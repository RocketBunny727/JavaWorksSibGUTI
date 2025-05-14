package com.example.searchservice.core.cache;

import com.example.searchservice.core.model.Catalog;
import com.example.searchservice.core.model.Product;
import com.example.searchservice.core.repository.ICatalogRepository;
import com.example.searchservice.core.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class SearchCacheInitializer implements CommandLineRunner {

    private final SearchCache searchCache;
    private final IProductRepository productRepository;
    private final ICatalogRepository catalogRepository;

    @Override
    public void run(String... args) {
        log.info("[SearchInitializer] Loading data into search cache...");
        searchCache.load(productRepository.findAll(), catalogRepository.findAll());
    }
}
