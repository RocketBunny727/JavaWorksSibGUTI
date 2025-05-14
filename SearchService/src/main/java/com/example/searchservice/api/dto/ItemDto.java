package com.example.searchservice.api.dto;

import com.example.searchservice.core.model.Item;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class ItemDto {
    private String name;
    private String image;
    private int price;
    private String description;

    public static ItemDto fromEntity(Item item) {
        return ItemDto.builder()
                .name(item.getName())
                .image(item.getImage())
                .price(item.getPrice())
                .description(item.getDescription())
                .build();
    }
}