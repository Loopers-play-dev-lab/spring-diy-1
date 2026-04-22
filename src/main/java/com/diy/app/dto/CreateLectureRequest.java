package com.diy.app.dto;

import com.diy.app.entity.Lecture;

import java.math.BigDecimal;

public class CreateLectureRequest {
    final String name;
    final BigDecimal price;

    private CreateLectureRequest() {
        this.name = null;
        this.price = null;
    }

    public CreateLectureRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }


    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Lecture toEntity() {
        return new Lecture(this.name, this.price);
    }
}
