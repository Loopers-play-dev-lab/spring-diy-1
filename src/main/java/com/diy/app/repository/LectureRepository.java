package com.diy.app.repository;

import com.diy.app.domain.Lecture;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// TODO: 제네릭 + 인터페이스화 시키기
public class LectureRepository {

    private static final Map<Long, Lecture> _map = new HashMap<>();

    public static Map<Long, Lecture> map = _map;

    public Optional<Lecture> findById(Long id) {
        return Optional.ofNullable(map.get(id));
    }

    public Collection<Lecture> findAll() {
        _map.values().forEach(it -> System.out.println("it1 == " + it));
        map.values().forEach(it -> System.out.println("it2 == " + it));
        return map.values();
    }

    public Lecture save(Lecture lecture) {
        _map.put(lecture.getId(), lecture);
        return map.get(lecture.getId());
    }

    public Long delete(Lecture lecture) {
        _map.remove(lecture.getId());
        return lecture.getId();
    }
}
