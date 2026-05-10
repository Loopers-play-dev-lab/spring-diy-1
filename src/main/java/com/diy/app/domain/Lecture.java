package com.diy.app.domain;


public class Lecture {
    private String lectureId;
    private final String name;
    private final int price;
    private boolean visible;

    public Lecture(final String LectureId, final String name, final int price) {
        this.lectureId = LectureId;
        this.name = name;
        this.price = price;
        this.visible = false;
    }

    public Lecture(final String LectureId, final String name, final int price, final boolean visible) {
        this.lectureId = LectureId;
        this.name = name;
        this.price = price;
        this.visible = visible;
    }

    private void changeVisible() {
        this.visible = true;
    }

    @MethodOrder(0)
    public String getLectureId() {
        return this.lectureId;
    }

    @MethodOrder(1)
    public String getName() {
        return this.name;
    }

    @MethodOrder(2)
    public int getPrice() {
        return this.price;
    }

    public void setLectureId(String lectureId) {
        this.lectureId = lectureId;
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "lectureId='" + lectureId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", visible=" + visible +
                '}';
    }
}