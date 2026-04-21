package com.diy.app.infrastructure;

import com.diy.app.domain.Lecture;
import java.util.ArrayList;
import java.util.List;

public class LectureRepository {

    private final List<Lecture> lectures;

    public LectureRepository() {
        this.lectures = new ArrayList<>();
    }

    public void save(Lecture lecture) {
        lecture.setId((long) (lectures.size() + 1));
        this.lectures.add(lecture);
    }

    public List<Lecture> getLectures() {
        return this.lectures;
    }

    public void delete(Long id) {
        lectures.removeIf(lecture -> lecture.getId().equals(id));
    }

    public void update(Long id, Lecture updated) {
        lectures.stream()
                .filter(lecture -> lecture.getId().equals(id))
                .findFirst()
                .ifPresent(lecture -> {
                    lecture.setName(updated.getName());
                    lecture.setPrice(updated.getPrice());
                });
    }
}
