package com.diy.app;

import com.diy.framework.web.annotations.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LectureRepository {
    private final Map<String, Lecture> lectureRepository = new ConcurrentHashMap<>();

    private List<Lecture> findAll() {
        return lectureRepository.values().stream().toList();
    }

    private Lecture findById(String id) {
        return lectureRepository.get(id);
    }

    private void save(Lecture lecture) {
        lectureRepository.put(lecture.getId(), lecture);
    }

    private void deleteById(String id) {
        lectureRepository.remove(id);
    }
}
