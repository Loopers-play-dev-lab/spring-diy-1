package com.diy.app.lecture;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LectureRepositoryImpl implements  LectureRepository {
    private final ConcurrentHashMap<Long, Lecture> repository = new ConcurrentHashMap<>();
    private static final AtomicLong sequence = new AtomicLong(0L);

    @Override
    public void save(Lecture lecture) {
        final Lecture newLecture = new Lecture(sequence.addAndGet(1L), lecture.getName(), lecture.getPrice());
        repository.putIfAbsent(newLecture.getId(), newLecture);
    }

    @Override
    public Lecture findById(long id) {
        if(!repository.containsKey(id)){
            throw new LectureException(LectureExceptionCode.NO_LECTURE_ID, id);
        }
        return repository.get(id);
    }

    @Override
    public Map<Long, Lecture> findAll() {
        return new HashMap<>(repository);
    }

    @Override
    public void delete(Lecture lecture) {
        if(!repository.containsKey(lecture.getId())){
            throw new LectureException(LectureExceptionCode.NO_LECTURE_ID, lecture.getId());
        }

        repository.remove(lecture.getId());
    }

    @Override
    public void update(Lecture lecture) {
        if(!repository.containsKey(lecture.getId())){
            throw new LectureException(LectureExceptionCode.NO_LECTURE_ID, lecture.getId());
        }

        final Lecture updatedLecture = new Lecture(lecture.getId(), lecture.getName(), lecture.getPrice());
        repository.put(updatedLecture.getId(), updatedLecture);
    }
}
