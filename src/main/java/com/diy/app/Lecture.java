package com.diy.app;

import java.util.Objects;

public class Lecture {
    long id;
    String name;
    int price;
    private boolean visible;

    public Lecture() {}

    public Lecture(long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Lecture(final String name, final int price) {
        this.name = name;
        this.price = price;
        this.visible = false;
    }

    public Lecture(final String name, final int price, final boolean visible) {
        this.name = name;
        this.price = price;
        this.visible = visible;
    }

    public long getId() {
        return id;
    }

    private void changeVisible() {
        this.visible = true;
    }

    @MethodOrder(1)
    public String getName() {
        return this.name;
    }

    @MethodOrder(2)
    public int getPrice() {
        return this.price;
    }

    public void setId(long id) {
        this.id = id;
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
