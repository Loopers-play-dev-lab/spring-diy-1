package com.diy.app.entity;

import java.math.BigDecimal;

public class Lecture {
    static Long lectureId = 1L;

    Long id;
    String name;
    BigDecimal price;

    public Lecture(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Lecture(String name, BigDecimal price) {
        this.name = name;
        this.id = lectureId++;
        this.price = price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
