package com.diy.app.repository;

import com.diy.app.domain.Lecture;

import javax.servlet.ServletException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LectureRepository {
    static LectureRepository instance;

    AtomicLong nextId;
    Map<Long, Lecture> lectureDB;

    private LectureRepository() {
        nextId = new AtomicLong(1L);
        lectureDB = new ConcurrentHashMap<>();
    }

    public static LectureRepository getInstance() {
        return instance == null ? instance = new LectureRepository() : instance;
    }

    public List<Lecture> getAll() {
        return lectureDB.values().stream().toList();
    }

    public Optional<Lecture> getById(long id) {
        return Optional.ofNullable(lectureDB.get(id));
    }

    public void insert(String name, long price) {
        Lecture lecture = new Lecture(nextId.getAndAdd(1L), name, price);
        lectureDB.put(lecture.getId(), lecture);
    }

    public void update(Lecture lecture) {
        if (!lectureDB.containsKey(lecture.getId())) throw new IllegalArgumentException("없는 ID");
        lectureDB.put(lecture.getId(), lecture);
    }

    public void delete(long id) {
        if (!lectureDB.containsKey(id)) throw new IllegalArgumentException("없는 ID");
        lectureDB.remove(id);
    }
}
