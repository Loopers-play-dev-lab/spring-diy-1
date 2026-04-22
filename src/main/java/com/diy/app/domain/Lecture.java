package com.diy.app.domain;

public class Lecture {
    long id;
    String name;
    int price;

    public Lecture(long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Lecture() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
