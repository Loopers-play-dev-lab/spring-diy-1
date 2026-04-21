package com.diy.app.domain;

import com.diy.app.presentation.LectureRequest;
import com.diy.app.presentation.LectureResponse;

import java.util.List;
import java.util.UUID;

public class LectureServiceImpl implements LectureService{
    private final LectureRepository lectureRepository;
    private static LectureServiceImpl instance;
    private LectureServiceImpl(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }
    public static LectureServiceImpl getInstance(LectureRepository lectureRepository) {
        if (instance == null) {
            instance = new LectureServiceImpl(lectureRepository);
        }
        return instance;
    }

    @Override
    public void add(LectureRequest request) {
        String lectureId = UUID.randomUUID().toString();
        Lecture lecture = new Lecture(lectureId, request.name(), request.price());
        lectureRepository.add(lecture);
    }

    @Override
    public List<Lecture> getLectures() {
        return lectureRepository.getLectures();
    }

    @Override
    public Lecture getLecture(String id) {
        return lectureRepository.getLecture(id);
    }

    @Override
    public void modify(LectureRequest request) {
        lectureRepository.modify(request.toLecture());
    }

    @Override
    public void delete(String lectureId) {
        lectureRepository.delete(lectureId);
    }
}
