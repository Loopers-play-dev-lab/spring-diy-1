package com.diy.app;

import java.util.Objects;

public class Lecture {
    private int id;
    private final String name;
    private final int price;
    private boolean visible;

    public Lecture() {
        this.name = null;
        this.price = 0;
        this.visible = false;
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

    private void changeVisible() {
        this.visible = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @MethodOrder(1)
    public String getName() {
        return this.name;
    }

    @MethodOrder(2)
    public int getPrice() {
        return this.price;
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
