package com.diy.app;

public class Lecture {
    private Long id;
    private String name;
    private Integer price;

    private Lecture(String name, Integer price) {
        this.id = null;
        this.name = name;
        this.price = price;
    }

    public static Lecture create(String name, Integer price) {
        return new Lecture(name, price);
    }

    public void update(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setId(Long newId) {
        id = newId;
    }
}
