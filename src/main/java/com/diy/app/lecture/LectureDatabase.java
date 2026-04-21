package com.diy.app.lecture;

import java.util.*;

public class LectureDatabase {
    private final Map<Long, Lecture> lectureTable = new HashMap<>();
    private Long nextId = 1L;

    public LectureDatabase() {
    }

    public Collection<Lecture> findAll() {
        return lectureTable.values();
    }

    public void insert(String title, String lecturer) {
        Long id = nextId++;
        Lecture lecture = new Lecture(id, title, lecturer);
        lectureTable.put(id, lecture);
    }

    public void update(Long id, String title, String lecturer) {
        Lecture lecture = new Lecture(id, title, lecturer);
        lectureTable.replace(id, lecture);
    }

    public void delete(Long id) {
        lectureTable.remove(id);
    }
}
