package com.diy.app.lecture;

import com.diy.app.lecture.request.CreateLectureRequest;

import com.diy.framework.annotation.Autowired;
import com.diy.framework.annotation.Component;
import java.util.List;

@Component
public class LectureService {

    private final LectureRepository lectureRepository;

    @Autowired
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
