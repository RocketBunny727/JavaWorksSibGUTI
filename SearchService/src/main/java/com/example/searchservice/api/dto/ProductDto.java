package com.example.searchservice.api.dto;

import com.example.searchservice.core.model.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {
    private String name;
    private String image;
    private int price;
    private String description;

    public static ProductDto fromEntity(Product product) {
        return ProductDto.builder()
                .name(product.getName())
                .image(product.getImage())
                .price(product.getPrice())
                .description(product.getDescription())
                .build();
    }
}
