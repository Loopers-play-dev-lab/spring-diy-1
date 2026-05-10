package com.diy.app.presentation;


import com.diy.app.domain.Lecture;
import com.diy.app.domain.LectureService;
import com.diy.framework.web.beans.annotations.Component;

import java.util.List;

@Component
public class LectureController {
    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
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
