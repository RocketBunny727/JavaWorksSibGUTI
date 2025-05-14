package com.example.searchservice.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchResponseDto {
    private List<String> suggestions;
    private List<CatalogDto> catalogs;
    private List<ItemDto> products;
}
