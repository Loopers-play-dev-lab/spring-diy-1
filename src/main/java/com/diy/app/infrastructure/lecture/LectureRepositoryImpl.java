package com.diy.app.infrastructure.lecture;

import com.diy.app.domain.Lecture.Lecture;
import com.diy.app.domain.Lecture.LectureRepository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LectureRepositoryImpl implements LectureRepository {
    private static final LectureRepositoryImpl INSTANCE = new LectureRepositoryImpl();
    private final ConcurrentHashMap<Long, Lecture> lectures = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong(0);

    private LectureRepositoryImpl() {}

    public static LectureRepositoryImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Collection<Lecture> getLectures() {
        return lectures.values();
    }

    @Override
    public Lecture save(final Lecture lecture) {
        lecture.setId(id.getAndIncrement());
        return lectures.putIfAbsent(lecture.getId(), lecture);
    }

    @Override
    public Lecture getLectureById(final long id) {
        return lectures.get(id);
    }
}
