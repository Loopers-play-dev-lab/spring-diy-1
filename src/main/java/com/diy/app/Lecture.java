package com.diy.app;

import java.math.BigDecimal;

public class Lecture {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private boolean visible;

    public static Lecture of(final Long id, final String name, final BigDecimal price) {
        return new Lecture(id, name, price);
    }

    private Lecture(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.visible = false;
    }

    @MethodOrder(1)
    public Long getId() {
        return id;
    }

    @MethodOrder(2)
    public String getName() {
        return name;
    }

    @MethodOrder(3)
    public BigDecimal getPrice() {
        return price;
    }

    @MethodOrder(4)
    public boolean isVisible() {
        return visible;
    }

    public boolean isNew() {
        return id == null;
    }

    public Lecture createWithId(final Long id) {
        return new Lecture(id, name, price);
    }

    private void changeVisible() {
        this.visible = true;
    }
}
