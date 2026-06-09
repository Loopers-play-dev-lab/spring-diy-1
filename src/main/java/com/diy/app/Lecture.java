package com.diy.app;

import java.math.BigDecimal;

public class Lecture {

    private Long id;
    private String name;
    private BigDecimal price;

    public Lecture() {
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

    public void setId(final Long id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public void validate() {
        if (this.id == null) {
            throw new IllegalArgumentException("id가 존재하지 않습니다.");
        }

        if (this.name == null || this.name.isBlank()) {
            throw new IllegalArgumentException("이름이 비어있습니다.");
        }

        if (this.price == null) {
            throw new IllegalArgumentException("가격이 존재하지 않습니다.");
        } else if (this.price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격이 음수일 수 없습니다.");
        }

    }
}
