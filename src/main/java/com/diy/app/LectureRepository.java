package com.diy.app;

import com.diy.framework.web.annotations.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LectureRepository {
    private final Map<String, Lecture> lectureRepository = new ConcurrentHashMap<>();

    public List<Lecture> findAll() {
        return lectureRepository.values().stream().toList();
    }

    public Lecture findById(String id) {
        return lectureRepository.get(id);
    }

    public void save(Lecture lecture) {
        lectureRepository.put(lecture.getId(), lecture);
    }

    public void deleteById(String id) {
        lectureRepository.remove(id);
    }
}
