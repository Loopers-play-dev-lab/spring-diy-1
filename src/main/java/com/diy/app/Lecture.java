package com.diy.app;

import java.math.BigDecimal;

public class Lecture {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    public static Lecture of(final Long id, final String name, final BigDecimal price) {
        return new Lecture(id, name, price);
    }

    private Lecture(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
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
