package com.diy.app.presentation;


import com.diy.app.domain.Lecture;
import com.diy.app.domain.LectureService;

import java.util.List;

public class LectureController {
    private final LectureService lectureService;

    private static LectureController instance;
    private LectureController(LectureService lectureService){
        this.lectureService = lectureService;
    }

    public static LectureController getInstance(LectureService lectureService){
        if (instance == null) {
            instance = new LectureController(lectureService);
        }
        return instance;
    }

    public void addLecture(LectureRequest request) {
        lectureService.add(request);
    }

    public void deleteLecture(String lectureId) {
        lectureService.delete(lectureId);
    }

    public List<Lecture> getLectures() {
        return lectureService.getLectures();
    }

    public LectureResponse getLecture(String id) {
        return LectureResponse.from(lectureService.getLecture(id));
    }

    public void updateLecture(LectureRequest request) {
        lectureService.modify(request);
    }
}
