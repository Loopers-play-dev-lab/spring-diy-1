package com.diy.app.domain;

public class Lecture {
    private final long id;
    private final String name;
    private final long price;

    public Lecture(long id, String name, long price) {
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

    public long getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return  "{id: " + this.id + ", name: " + this.name + ", price: " + this.price + "}";
    }
}
