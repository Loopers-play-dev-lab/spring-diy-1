package com.diy.framework.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LectureRepository {

    private static final Map<Long, Lecture> _map = new HashMap<>();

    public static Map<Long, Lecture> map = _map;

    public Lecture findById(Long id) {
        return map.get(id);
    }

    public Collection<Lecture> findAll() {
        _map.values().forEach(it -> System.out.println("it1 == " + it));
        map.values().forEach(it -> System.out.println("it2 == " + it));
        return map.values();
    }

    public Lecture save(Lecture lecture) {
        System.out.println("lecture = " + lecture);
        _map.put(lecture.getId(), lecture);
        return map.get(lecture.getId());
    }

    public Long delete(Lecture lecture) {
        _map.remove(lecture.id);
        return lecture.id;
    }
}
