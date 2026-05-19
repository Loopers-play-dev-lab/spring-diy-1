package com.diy.app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class LecturePutRequest {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    @JsonCreator
    private LecturePutRequest(@JsonProperty("id") final Long id,
                              @JsonProperty("name") final String name,
                              @JsonProperty("price") final BigDecimal price) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Lecture toLecture() {
        return Lecture.of(id, name, price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
