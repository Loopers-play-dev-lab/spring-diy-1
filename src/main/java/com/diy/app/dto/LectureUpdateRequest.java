package com.diy.app.dto;

import com.diy.app.Lecture;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class LectureUpdateRequest {
    private Long id;
    private String name;
    private BigDecimal price;

    @JsonCreator
    public LectureUpdateRequest(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name,
            @JsonProperty("price") BigDecimal price) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("잘못된 ID 형식입니다.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 필수 입력 항목입니다.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }

        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Lecture toLecture(){
        return new Lecture(this.id, this.name, this.price);
    }

    @Override
    public String toString() {
        return "LectureUpdateRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
