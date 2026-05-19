package com.diy.app.repository;

import com.diy.app.domain.Lecture;
import com.diy.framework.web.bean.annotation.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryLectureRepositoryImplDummy implements LectureRepository {
    private final Map<Long, Lecture> mapRepository = new ConcurrentSkipListMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public List<Lecture> find() {
        return mapRepository.values().stream().toList();
    }

    @Override
    public Lecture save(Lecture lecture) {
        long id = sequence.incrementAndGet();
        Lecture savedLecture = lecture.setId(id);
        mapRepository.put(id, savedLecture);
        return savedLecture;
    }

    @Override
    public Lecture updateById(Long id, Lecture updatelecture) {
        Lecture targetLecture = mapRepository.get(id);
        mapRepository.put(targetLecture.getId(), updatelecture);
        return updatelecture;
    }

    @Override
    public Lecture deleteById(Long id) {
        return mapRepository.remove(id);
    }
}
