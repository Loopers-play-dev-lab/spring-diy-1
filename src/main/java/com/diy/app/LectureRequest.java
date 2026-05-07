package com.diy.app;

import java.math.BigDecimal;

public class LectureRequest {
    private String name;
    private int price;

    public LectureRequest() {}

    public Lecture toLecture(Long id) {
        return Lecture.of(id, name, BigDecimal.valueOf(price));
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
