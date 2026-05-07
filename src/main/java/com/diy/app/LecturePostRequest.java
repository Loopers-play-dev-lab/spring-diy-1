package com.diy.app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class LecturePostRequest {
    private final String name;
    private final BigDecimal price;

    @JsonCreator
    private LecturePostRequest(@JsonProperty("name") final String name,
                               @JsonProperty("price") final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Lecture toLecture(Long id) {
        return Lecture.of(id, name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
