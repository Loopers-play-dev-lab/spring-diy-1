package com.diy.framework.web;

import org.jetbrains.annotations.NotNull;

// TODO: 이거 프레임워크 말고 app 으로 옮기는 게 좋을 듯
public class Lecture {
    private static long idOffset = 0L;

    Long id;
    String name;
    Price price;

    public Lecture(String name, Price price) {
        this.id = ++idOffset;
        this.name = name;
        this.price = price;
    }

    public static Lecture open(
            String name,
            Price price
    ) {
        return new Lecture(
                name,
                price
        );
    }

    public Lecture edit(
            String name,
            Price price
    ) {
        this.name = name;
        this.price = price;

        return this;
    }

    public record Price(
       long value
    ) {
        public static Price of(long value) {
            return new Price(value);
        }

        @NotNull
        @Override
        public String toString() {
            return Long.valueOf(this.value).toString();
        }
    }

    public Long getId() {
        return id;
    }

    public Price getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "id = " + id + ", name = " + name + ", price = " + price;
    }
}
