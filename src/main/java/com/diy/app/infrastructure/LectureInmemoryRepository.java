package com.diy.app.infrastructure;

import com.diy.app.domain.Lecture;
import com.diy.app.domain.LectureRepository;
import com.diy.module.database.InMemoryDatabase;

import java.util.List;
import java.util.UUID;

public class LectureInmemoryRepository implements LectureRepository {
    private final InMemoryDatabase database;
    private LectureInmemoryRepository(InMemoryDatabase database) {
        this.database = database;
    }
    private static LectureInmemoryRepository instance;
    public static LectureInmemoryRepository getInstance(InMemoryDatabase database) {
        if (instance == null) {
            instance = new LectureInmemoryRepository(database);
        }
        return instance;
    }

    @Override
    public void add(Lecture lecture) {
        String lectureId = "lecture-" + UUID.randomUUID();
        lecture.setLectureId(lectureId);
        database.create(lectureId, lecture);
        System.out.println(database.get(lectureId));
    }

    @Override
    public List<Lecture> getLectures() {
        return database.getAll("lecture-").stream().map(Lecture.class::cast).toList();
    }

    @Override
    public Lecture getLecture(String id) {
        System.out.println(database.get(id));
        Lecture lecture = (Lecture) database.get(id);
        lecture.setLectureId(id);
        return lecture;
    }

    @Override
    public void modify(Lecture lecture) {
        database.update(lecture.getLectureId(), lecture);
    }

    @Override
    public void delete(String lectureId) {
        database.delete(lectureId);
    }
}
