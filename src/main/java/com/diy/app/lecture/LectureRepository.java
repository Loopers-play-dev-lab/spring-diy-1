package com.diy.app.lecture;

import com.diy.framework.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class LectureRepository {

    private final List<Lecture> lectures = new ArrayList<>();

    public LectureRepository() {
        lectures.add(new Lecture("스프링 DIY", 30000, true));
        lectures.add(new Lecture("리액트 입문", 25000));
    }

    public List<Lecture> findAll() {
        return Collections.unmodifiableList(lectures);
    }

    public void save(final Lecture lecture) {
        lectures.add(lecture);
    }

    public boolean containsName(final String name) {
        return lectures.stream().anyMatch(lecture -> lecture.getName().equals(name));
    }

    public Lecture findByName(final String name) {
        return lectures.stream()
                .filter(lecture -> lecture.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public boolean update(final String originalName, final Lecture updatedLecture) {
        int index = indexOf(originalName);
        if (index < 0) {
            return false;
        }

        lectures.set(index, updatedLecture);
        return true;
    }

    public boolean deleteByName(final String name) {
        int index = indexOf(name);
        if (index < 0) {
            return false;
        }

        lectures.remove(index);
        return true;
    }

    private int indexOf(final String name) {
        for (int i = 0; i < lectures.size(); i++) {
            if (lectures.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
