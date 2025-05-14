package com.example.searchservice.core.service;

import com.example.searchservice.api.dto.CatalogDto;
import com.example.searchservice.api.dto.ItemDto;
import com.example.searchservice.api.dto.ProductDto;
import com.example.searchservice.core.cache.SearchCache;
import com.example.searchservice.core.exception.DatabaseConflictException;
import com.example.searchservice.core.model.Catalog;
import com.example.searchservice.core.model.Item;
import com.example.searchservice.core.model.Product;
import com.example.searchservice.core.repository.ICatalogRepository;
import com.example.searchservice.core.repository.IProductRepository;
import com.example.searchservice.core.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsertService {

    private final ICatalogRepository catalogRepository;
    private final ItemRepository itemRepository;
    private final IProductRepository productRepository;
    private final SearchCache searchCache;

    public Catalog insertCatalog(CatalogDto catalogDto) {
        try {
            if (catalogRepository.existsByName(catalogDto.getName())) {
                throw new DatabaseConflictException("Catalog with this name already exists");
            }

            Catalog catalog = new Catalog();
            catalog.setName(catalogDto.getName());
            catalog.setImage(catalogDto.getImage());
            catalog.setDescription(catalogDto.getDescription());

            List<Item> items = catalogDto.getItems().stream()
                    .map(this::mapToItem)
                    .collect(Collectors.toList());

            itemRepository.saveAll(items);
            catalog.setItems(items);

            Catalog saved = catalogRepository.save(catalog);
            searchCache.addCatalog(saved);
            log.info("Catalog added to search cache: {}", saved.getName());
            return saved;
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseConflictException("Integrity constraint violated while inserting catalog", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while inserting catalog", e);
        }
    }

    public Product insertProduct(ProductDto productDto) {
        try {
            if (productRepository.existsByName(productDto.getName())) {
                throw new DatabaseConflictException("Product with this name already exists");
            }

            Product product = new Product();
            product.setName(productDto.getName());
            product.setImage(productDto.getImage());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());

            Product saved = productRepository.save(product);
            searchCache.addProduct(saved);
            log.info("Product added to search cache: {}", saved.getName());
            return saved;
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseConflictException("Integrity constraint violated while inserting product", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while inserting product", e);
        }
    }

    public Catalog updateCatalog(Long id, CatalogDto catalogDto) {
        try {
            Catalog catalog = catalogRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Catalog not found with id: " + id));

            if (catalogRepository.existsByName(catalogDto.getName()) &&
                    !catalog.getName().equals(catalogDto.getName())) {
                throw new DatabaseConflictException("Catalog with this name already exists");
            }

            catalog.setName(catalogDto.getName());
            catalog.setImage(catalogDto.getImage());
            catalog.setDescription(catalogDto.getDescription());

            List<Item> updatedItems = catalogDto.getItems().stream()
                    .map(this::mapToItem)
                    .collect(Collectors.toList());

            catalog.getItems().clear();
            catalog.getItems().addAll(updatedItems);

            itemRepository.saveAll(updatedItems);
            Catalog saved = catalogRepository.save(catalog);

            searchCache.removeCatalog(catalog);
            searchCache.addCatalog(saved);
            log.info("Catalog updated in search cache: {}", saved.getName());
            return saved;
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseConflictException("Integrity constraint violated while updating catalog", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while updating catalog", e);
        }
    }

    public Product updateProduct(Long id, ProductDto productDto) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

            if (productRepository.existsByName(productDto.getName()) &&
                    !product.getName().equals(productDto.getName())) {
                throw new DatabaseConflictException("Product with this name already exists");
            }

            product.setName(productDto.getName());
            product.setImage(productDto.getImage());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());

            Product saved = productRepository.save(product);
            searchCache.removeProduct(product);
            searchCache.addProduct(saved);
            log.info("Product updated in search cache: {}", saved.getName());
            return saved;
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseConflictException("Integrity constraint violated while updating product", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while updating product", e);
        }
    }

    public void deleteCatalog(Long id) {
        try {
            Catalog catalog = catalogRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Catalog not found with id: " + id));

            catalogRepository.delete(catalog);
            searchCache.removeCatalog(catalog);
            log.info("Catalog removed from search cache: {}", catalog.getName());
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseConflictException("Catalog cannot be deleted due to database constraint", e);
        }
    }

    public void deleteProduct(Long id) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

            productRepository.delete(product);
            searchCache.removeProduct(product);
            log.info("Product removed from search cache: {}", product.getName());
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseConflictException("Product cannot be deleted due to database constraint", e);
        }
    }

    private Item mapToItem(ItemDto dto) {
        if (itemRepository.existsByName(dto.getName())) {
            return itemRepository.findByName(dto.getName());
        } else {
            Item item = new Item();
            item.setName(dto.getName());
            item.setImage(dto.getImage());
            item.setPrice(dto.getPrice());
            item.setDescription(dto.getDescription());

            Item saved = itemRepository.save(item);
            return saved;
        }
    }
}
