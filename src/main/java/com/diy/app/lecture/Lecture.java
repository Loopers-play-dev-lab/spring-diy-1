package com.diy.app.lecture;

public class Lecture {
    private Long id;
    private String title;
    private String lecturer;

    public Lecture(Long id, String title, String lecturer) {
        this.id = id;
        this.title = title;
        this.lecturer = lecturer;
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getLecturer() {
        return this.lecturer;
    }
}
