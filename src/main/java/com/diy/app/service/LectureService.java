package com.diy.app.service;

import com.diy.app.domain.Lecture;
import com.diy.app.domain.LectureRequest;
import com.diy.app.repository.LectureRepository;

import java.util.List;
import java.util.Optional;

public class LectureService {
    private final LectureRepository repository = LectureRepository.INSTANCE;

    public Lecture register(LectureRequest request) {
        return repository.save(request);
    }

    public List<Lecture> getAll() {
        return repository.findAll();
    }

    public Optional<Lecture> getOne(Long id) {
        return repository.findById(id);
    }

    public Optional<Lecture> modify(Long id, LectureRequest request) {
        return repository.update(id, request);
    }

    public boolean remove(Long id) {
        return repository.deleteById(id);
    }
}
