package com.diy.app.domain;

public class Lecture {
    long id;
    String name;
    int price;

    public Lecture(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public static Lecture create(String name, int price) {
        return new Lecture(name, price);
    }

    public void put(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
