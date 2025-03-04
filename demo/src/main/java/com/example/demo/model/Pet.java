package com.example.demo.model;

import java.util.List;

public record Pet (
        int id,
        String name,
        Category category,
        List<Tag> tags,
        Status status
        ) {}
