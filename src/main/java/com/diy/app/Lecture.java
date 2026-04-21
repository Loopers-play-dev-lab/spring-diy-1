package com.diy.app;

import java.math.BigDecimal;

public class Lecture {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    public Lecture(Long id, String name, BigDecimal price) {
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("이름은 필수 입력 항목입니다.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        }

        this.id = id;
        this.name = name;
        this.price = price;
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

    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
