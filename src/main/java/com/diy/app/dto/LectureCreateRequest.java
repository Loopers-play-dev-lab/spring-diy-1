package com.diy.app.dto;

import com.diy.app.Lecture;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class LectureCreateRequest {

    private String name;
    private BigDecimal price;

    @JsonCreator
    public LectureCreateRequest(
            @JsonProperty("name") String name,
            @JsonProperty("price") BigDecimal price) {
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("이름은 필수 입력 항목입니다.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }

        this.name = name;
        this.price = price;
    }

    public Lecture toLecture(){
        return new Lecture(null, this.name, this.price);
    }

    @Override
    public String toString() {
        return "LectureCreateRequest{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
