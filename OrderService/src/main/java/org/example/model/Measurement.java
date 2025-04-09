package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Measurement {

    @JsonProperty("measurementType")
    @Enumerated(EnumType.STRING)
    @Column(name = "measure_type")
    private MeasureType measurementType;

    @JsonProperty("name")
    @Column(name = "measure_name")
    private String name;

    @JsonProperty("symbol")
    @Column(name = "measure_symbol")
    private String symbol;
}