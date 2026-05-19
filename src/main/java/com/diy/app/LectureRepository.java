package com.diy.app;

import com.diy.framework.context.annotation.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class LectureRepository {

    private static final ConcurrentHashMap<Long, Lecture> lectureRepository = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public Optional<Lecture> findById(final Long id) {
        return Optional.ofNullable(lectureRepository.get(id));
    }

    public Collection<Lecture> findAll() {
        return lectureRepository.values();
    }

    public void save(final Lecture lecture) {
        if (lecture.isNew()) {
            final long newLectureId = generateNewId();
            lectureRepository.put(newLectureId, lecture.createWithId(newLectureId));
            return;
        }
        lectureRepository.put(lecture.getId(), lecture);
    }

    public void remove(final Long lectureId) {
        lectureRepository.remove(lectureId);
    }

    public boolean exists(final Long lectureId) {
        return lectureRepository.containsKey(lectureId);
    }

    private long generateNewId() {
        return idGenerator.incrementAndGet();
    }
}
