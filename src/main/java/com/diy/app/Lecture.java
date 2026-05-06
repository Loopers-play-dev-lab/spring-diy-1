package com.diy.app;

import com.diy.framework.web.context.annotation.MethodOrder;

import java.math.BigDecimal;

public class Lecture {
    private static int seq = 0;

    private int id;
    private String name;
    private BigDecimal price;
    private boolean visible;

    private Lecture(final String name, final BigDecimal price) {
        this.id = ++seq;
        this.name = name;
        this.price = price;
        this.visible = false;
    }

    private Lecture(final String name, final BigDecimal price, final boolean visible) {
        this.id = ++seq;
        this.name = name;
        this.price = price;
        this.visible = visible;
    }

    public static Lecture register(final String name, final BigDecimal price) {
        return new Lecture(name, price);
    }

    private void changeVisible() {
        this.visible = true;
    }

    public int getId() {
        return id;
    }

    @MethodOrder(1)
    public String getName() {
        return this.name;
    }

    @MethodOrder(2)
    public BigDecimal getPrice() {
        return this.price;
    }

    public void updateName(final String newName) {
        this.name = newName;
    }

    public void updatePrice(final BigDecimal newPrice) {
        this.price = newPrice;
    }
}
