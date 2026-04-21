package com.diy.app.domain;

public class Lecture {
    private String lectureId;
    private String name;

    public String getLectureId() {
        return lectureId;
    }

    public void setLectureId(String lectureId) {
        this.lectureId = lectureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    private Integer price;

    public Lecture(String lectureId, String name, Integer price) {
        this.lectureId = lectureId;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "lectureId='" + lectureId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
