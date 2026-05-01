package com.diy.app.business.repository;

import com.diy.app.business.domain.Lecture;
import com.diy.framework.beans.annotations.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
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

    public Lecture insert(String name, long price) {
        Lecture lecture = new Lecture(nextId.getAndAdd(1L), name, price);
        lectureDB.put(lecture.getId(), lecture);
        return lecture;
    }

    public int update(Lecture lecture) {
        if (lectureDB.containsKey(lecture.getId())) {
            lectureDB.put(lecture.getId(), lecture);
            return 1;
        }
        return 0;
    }

    public int delete(long id) {
        return lectureDB.remove(id) == null ? 0 : 1;
    }
}
