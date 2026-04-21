package com.diy.app;

import java.util.UUID;

public class Lecture{
    private String id;
    private String name;
    private int price;

    public Lecture() {
    }

    public Lecture(String id, String name, int price) {
        this(name, price);
        this.id = id;
    }

    public Lecture(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Lecture toLecture() {
        return new Lecture(
            UUID.randomUUID().toString(),
            this.name,
            this.price
        );
    }

    public void update(Lecture newLecture) {
        this.name = newLecture.name;
        this.price = newLecture.price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
