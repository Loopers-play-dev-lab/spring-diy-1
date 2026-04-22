package com.diy.app;

import java.util.List;

public class LectureService {

    private final LectureRepository repository;

    public LectureService(LectureRepository repository) {
        this.repository = repository;
    }

    public Lecture register(Lecture lecture) {
        return repository.save(lecture);
    }

    public List<Lecture> findAll() {
        return repository.findAll();
    }

    public boolean update(int id, Lecture patch) {
        return repository.findById(id)
                .map(lec -> {
                    lec.setName(patch.getName());
                    lec.setPrice(patch.getPrice());
                    return true;
                })
                .orElse(false);
    }

    public boolean delete(int id) {
        return repository.deleteById(id);
    }
}
