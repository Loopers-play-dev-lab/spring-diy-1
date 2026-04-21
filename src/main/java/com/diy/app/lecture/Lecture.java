package com.diy.app.lecture;

public class Lecture {

    private Long id;

    private String name;

    private int price;

    public Lecture(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Lecture(Long id, String name, int price) {
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

    public int getPrice() {
        return price;
    }
}
