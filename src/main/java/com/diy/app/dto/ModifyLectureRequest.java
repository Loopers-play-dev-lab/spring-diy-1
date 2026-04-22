package com.diy.app.dto;

import com.diy.app.entity.Lecture;

import java.math.BigDecimal;

public class ModifyLectureRequest {
    final String name;
    final Long id;
    final BigDecimal price;

    private ModifyLectureRequest() {
        this.name = null;
        this.id = null;
        this.price = null;
    }

    public ModifyLectureRequest(String name, Long id, BigDecimal price) {
        this.name = name;
        this.id = id;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Lecture toEntity() {
        return new Lecture(this.id, this.name, this.price);
    }
}
