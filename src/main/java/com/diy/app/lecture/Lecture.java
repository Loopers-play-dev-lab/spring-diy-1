package com.diy.app.lecture;

import java.math.BigDecimal;

public class Lecture {
    private Long id;
    private String name;
    private BigDecimal price;

    public Lecture(){}

    public Lecture(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
