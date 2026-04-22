package com.diy.app.lecture;

public class Lecture {

    private long id;
    private String name;
    private int price;

    public Lecture(String title, int price) {
        this.name = title;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}