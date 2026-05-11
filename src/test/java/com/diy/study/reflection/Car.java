package com.diy.study.reflection;

import java.math.BigDecimal;

public class Car {
    private String name;
    private BigDecimal price;

    public Car() {}

    public Car(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    @PrintView
    public void printView() {
        System.out.println("자동차 정보를 출력 합니다.");
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String testGetName() {
        return "test name: " + name;
    }

    public String testGetPrice() {
        return "test price: " + price;
    }
}
