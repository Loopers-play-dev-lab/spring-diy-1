package com.diy.app.lecture;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class LectureRepositoryImpl implements  LectureRepository {
    private final ConcurrentHashMap<Long, Lecture> repository = new ConcurrentHashMap<>();
    private static final AtomicLong sequence = new AtomicLong(0L);

    @Override
    public void save(Lecture lecture) {
        final Lecture newLecture = new Lecture(sequence.addAndGet(1L), lecture.getName(), lecture.getPrice());
        repository.put(newLecture.getId(), newLecture);
    }

    @Override
    public Lecture findById(long id) {
        Lecture lecture = repository.get(id);
        if(lecture == null){
            throw new LectureException(LectureExceptionCode.NO_LECTURE_ID, id);
        }
        return lecture;
    }

    @Override
    public Map<Long, Lecture> findAll() {
         return repository.values().stream()
              .sorted(Comparator.comparingLong(Lecture::getId))
              .collect(Collectors.toMap(
                      Lecture::getId, l -> l,
                      (a, b) -> a, LinkedHashMap::new));
    }

    @Override
    public void delete(Lecture lecture) {
        if(repository.remove(lecture.getId()) == null){
            throw new LectureException(LectureExceptionCode.NO_LECTURE_ID, lecture.getId());
        }
    }

    @Override
    public void update(Lecture lecture) {
        Lecture updated = new Lecture(lecture.getId(), lecture.getName(), lecture.getPrice());
        if (repository.replace(lecture.getId(), updated) == null) {
            throw new LectureException(LectureExceptionCode.NO_LECTURE_ID, lecture.getId());
        }
    }
}
