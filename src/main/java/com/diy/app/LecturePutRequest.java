package com.diy.app;

import java.math.BigDecimal;

public class LecturePutRequest {
    private Long id;
    private String name;
    private BigDecimal price;

    public LecturePutRequest() {}

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
