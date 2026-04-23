package com.diy.app;

public class Lecture {

    public Long id;
    public String title;
    public String description;

    public Lecture() {
    }

    public Lecture(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
