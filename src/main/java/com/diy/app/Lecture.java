package com.diy.app;

import java.util.Objects;

public class Lecture {
    private int id;
    private String name;
    private int price;

    Lecture() {}

    Lecture(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Lecture lecture = (Lecture) o;
        return id == lecture.id && price == lecture.price && Objects.equals(name, lecture.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}
