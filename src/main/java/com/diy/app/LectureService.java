package com.diy.app;

import com.diy.framework.web.beans.annotations.Autowired;
import com.diy.framework.web.beans.annotations.Component;
import java.util.Collection;

@Component
public class LectureService {

    private final LectureRepository repository;

    @Autowired
    public LectureService(LectureRepository repository) {
        this.repository = repository;
    }

    public void save(final Lecture lecture) {
        repository.save(lecture);
    }

    public Collection<Lecture> findAll() {
        return repository.findAll();
    }
}
