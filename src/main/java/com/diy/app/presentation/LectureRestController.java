package com.diy.app.presentation;

import com.diy.app.domain.Lecture;
import com.diy.app.domain.LectureService;
import com.diy.framework.web.beans.annotations.RestController;

import java.util.Arrays;
import java.util.Map;

@RestController(url = "/lecture")
public class LectureRestController {
    public LectureRestController(LectureService lectureService) {
        this.lectureService = lectureService;
    }
    private final LectureService lectureService;

    public Object handleRequest(String method, Map<String, ?> params) throws IllegalArgumentException {
        System.out.println("LectureRestController handleRequest method : " + method);

        switch (method) {
            case "GET" : return doGet(params);
            default : throw new IllegalArgumentException("Method not supported");
        }
    }

    public Lecture doGet(Map<String, ?> params) {
        String lectureId = Arrays.toString((String[])params.get("lectureId")).replace("[", "").replace("]", "");
        return lectureService.getLecture(lectureId);
    }
}
