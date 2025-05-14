package com.example.searchservice.api.dto;

import com.example.searchservice.core.model.Catalog;
import lombok.Data;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CatalogDto {
    private String name;
    private String image;
    private String description;
    private List<ItemDto> items;

    public static CatalogDto fromEntity(Catalog catalog) {
        return CatalogDto.builder()
                .name(catalog.getName())
                .image(catalog.getImage())
                .description(catalog.getDescription())
                .items(catalog.getItems().stream()
                        .map(ItemDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
