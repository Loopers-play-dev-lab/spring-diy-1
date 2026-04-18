package com.diy.app;

public class Lecture {
    public Long id;
    public String title;
    public String instructor;

    public Lecture() {
    }

    public Lecture(Long id, String title, String instructor) {
        this.id = id;
        this.title = title;
        this.instructor = instructor;
    }
}
