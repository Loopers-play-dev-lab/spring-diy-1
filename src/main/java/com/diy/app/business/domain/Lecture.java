package com.diy.app.business.domain;

public class Lecture {
    private long id;
    private String name;
    private long price;

    public Lecture() {
    }

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

    public void update(Lecture lecture) {
        this.name = lecture.name;
        this.price = lecture.price;
    }

    @Override
    public String toString() {
        return  "{id: " + this.id + ", name: " + this.name + ", price: " + this.price + "}";
    }
}
