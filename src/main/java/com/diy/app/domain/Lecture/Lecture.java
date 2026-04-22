package com.diy.app.domain.Lecture;

public class Lecture {
    long id;
    String name;
    long price;

    public Lecture(String name, long price) {
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

    public long getPrice() {
        return price;
    }
}
