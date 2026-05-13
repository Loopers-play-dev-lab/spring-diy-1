package com.diy.app;

import java.math.BigDecimal;

public class Lecture {

    private Long id;
    private String name;
    private BigDecimal price;

    public Lecture() {
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

    public void setId(Long id) {
        this.id = id;
    }
}
