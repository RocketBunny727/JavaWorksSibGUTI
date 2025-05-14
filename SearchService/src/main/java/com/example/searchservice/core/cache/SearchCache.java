package com.example.searchservice.core.cache;

import com.example.searchservice.core.model.Catalog;
import com.example.searchservice.core.model.Item;
import com.example.searchservice.core.model.Product;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Component
public class SearchCache {

    private final NavigableMap<String, Long> productMap = new TreeMap<>();
    private final NavigableMap<String, Long> catalogMap = new TreeMap<>();
    private final NavigableMap<String, Long> itemMap = new TreeMap<>();

    public synchronized void load(List<Product> products, List<Catalog> catalogs) {
        log.info("[SearchCache] Initializing in-memory search cache");

        productMap.clear();
        catalogMap.clear();
        itemMap.clear();

        products.forEach(p -> productMap.put(p.getName().toLowerCase(), p.getId()));
        catalogs.forEach(c -> {
            catalogMap.put(c.getName().toLowerCase(), c.getId());
            if (c.getItems() != null) {
                c.getItems().forEach(i -> itemMap.put(i.getName().toLowerCase(), c.getId()));
            }
        });

        log.info("[SearchCache] Cache loaded: {} products, {} catalogs, {} items",
                productMap.size(), catalogMap.size(), itemMap.size());
    }

    public synchronized void addProduct(Product p) {
        productMap.put(p.getName().toLowerCase(), p.getId());
        log.info("[SearchCache] Added product: {} ({})", p.getName(), p.getId());
    }

    public synchronized void removeProduct(Product p) {
        productMap.remove(p.getName().toLowerCase());
        log.info("[SearchCache] Removed product: {} ({})", p.getName(), p.getId());
    }

    public synchronized void addCatalog(Catalog c) {
        catalogMap.put(c.getName().toLowerCase(), c.getId());
        if (c.getItems() != null) {
            c.getItems().forEach(i -> itemMap.put(i.getName().toLowerCase(), c.getId()));
        }
        log.info("[SearchCache] Added catalog: {} ({})", c.getName(), c.getId());
    }

    public synchronized void removeCatalog(Catalog c) {
        catalogMap.remove(c.getName().toLowerCase());
        if (c.getItems() != null) {
            c.getItems().forEach(i -> itemMap.remove(i.getName().toLowerCase()));
        }
        log.info("[SearchCache] Removed catalog: {} ({})", c.getName(), c.getId());
    }

    public List<String> prefixSearch(NavigableMap<String, Long> map, String prefix, int limit) {
        String from = prefix.toLowerCase();
        String to = from + Character.MAX_VALUE;
        return map.subMap(from, true, to, true)
                .keySet()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
}
