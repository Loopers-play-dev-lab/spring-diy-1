package com.diy.app;

import com.diy.framework.web.annotation.Component;
import com.diy.framework.web.annotation.MethodOrder;

import java.math.BigDecimal;

@Component
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

    public void setId(Long id) {
        this.id = id;
    }

    @MethodOrder(1)
    private String getDisplayName() {
        return "lecture";
    }
}
