package com.diy.app.business.service;

import com.diy.app.business.domain.Lecture;
import com.diy.app.business.repository.LectureRepository;

import java.util.List;

public class LectureService {
    private static LectureService instance = null;

    private static LectureRepository repository = null;

    private LectureService(LectureRepository repository) {
        this.repository = repository;
    }

    public static LectureService getInstance() {
        return instance == null ? instance = new LectureService(LectureRepository.getInstance()) : instance;
    }

    public List<Lecture> getAllLectures() {
        return repository.getAll();
    }

    public Lecture getLectureById(long id) {
        return repository.getById(id).orElseThrow();
    }

    public void create(String name, long price) {
        repository.insert(name, price);
    }

    public void update(Lecture lecture) {
        if (repository.update(lecture) == 0) throw new IllegalArgumentException("존재하지 않는 ID입니다.");
    }

    public void delete(long id) {
        if (repository.delete(id) == 0) throw new IllegalArgumentException("존재하지 않는 ID입니다.");
    }
}
