package com.diy.app.Entity;

import com.diy.app.annotation.Component;

import java.math.BigDecimal;

@Component
public class Lecture {
    private  Long id;
    private  String name;
    private  BigDecimal price;

    public Lecture() {}
    public Lecture(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
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

    private String printView() {
        return "Lecture{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
