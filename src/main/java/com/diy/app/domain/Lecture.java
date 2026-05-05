package com.diy.app.domain;

import com.diy.framework.jpa.annotation.Entity;
import com.diy.framework.jpa.annotation.GeneratedValue;
import com.diy.framework.jpa.annotation.Id;

// TODO: Entity, Id 등 인터페이스화 -> 어노테이션으로 만들기
@Entity
public class Lecture {
    private static long idOffset = 0L;

    @Id @GeneratedValue
    Long id;

    String name;

    Price price;

    protected Lecture() {
    }

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
