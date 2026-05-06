package com.diy.app;

import com.diy.framework.web.beans.annotations.Component;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class LectureRepository {

    private final Map<Long, Lecture> store = new HashMap<>();

    public void save(final Lecture lecture) {
        lecture.setId((long) store.size());
        store.put(lecture.getId(), lecture);
    }

    public Collection<Lecture> findAll() {
        return store.values();
    }
}
