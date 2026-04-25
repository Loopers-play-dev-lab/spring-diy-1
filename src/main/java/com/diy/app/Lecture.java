package com.diy.app;

import java.math.BigDecimal;

public class Lecture {
    private  Long id;
    private  String name;
    private  BigDecimal price;

    public Lecture() {}
    public Lecture(Long id, String name, BigDecimal price) {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
