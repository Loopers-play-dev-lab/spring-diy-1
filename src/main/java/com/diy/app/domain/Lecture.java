package com.diy.app.domain;

import com.diy.app.servlet.dto.request.LecturePost;

import java.math.BigDecimal;

public final class Lecture {
    private final Long id;
    private final String name;
    private final BigDecimal price;


    public Lecture(
            Long id,
            String name,
            BigDecimal price
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Lecture makeForSave(LecturePost lecturePost) {
        return new Lecture(-1L, lecturePost.name(), lecturePost.price());
    }

    public Lecture setId(Long id) {
        return new Lecture(id, name, price);
    }

    public Lecture update(String name, BigDecimal price) {
        return new Lecture(id, name, price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
