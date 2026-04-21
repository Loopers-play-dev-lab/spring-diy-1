package com.diy.app;

public class Lecture {
    private final long id;
    private final String name;
    private final double price;

    public Lecture(long id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
