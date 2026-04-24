package com.diy.app.servlet.dto.response;

import com.diy.app.domain.Lecture;

import java.math.BigDecimal;

public class LectureResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    private LectureResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static LectureResponse from(Lecture lecture){
        return new LectureResponse(lecture.getId(), lecture.getName(), lecture.getPrice());
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
