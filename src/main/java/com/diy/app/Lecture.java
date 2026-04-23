package com.diy.app;

import java.math.BigDecimal;

public class Lecture {
    private static int seq = 0;

    private int id;
    private String name;
    private BigDecimal price;

    private Lecture(final String name, final BigDecimal price) {
        this.id = ++seq;
        this.name = name;
        this.price = price;
    }

    public static Lecture register(final String name, final BigDecimal price) {
        return new Lecture(name, price);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void updateName(final String newName) {
        this.name = newName;
    }

    public void updatePrice(final BigDecimal newPrice) {
        this.price = newPrice;
    }
}
