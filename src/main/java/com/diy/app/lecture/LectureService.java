package com.diy.app.lecture;

import com.diy.app.lecture.request.CreateLectureRequest;

import java.util.List;

public class LectureService {

    private final LectureRepository lectureRepository;

    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public void createLecture(CreateLectureRequest createRequest) {
        Lecture lecture = new Lecture(createRequest.name(), createRequest.price());
        lectureRepository.insert(lecture);
    }

    public List<Lecture> getAllLectures() {
        return lectureRepository.findAll();
    }
}
