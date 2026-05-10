package com.diy.app.presentation;

import com.diy.app.domain.Lecture;
import com.diy.app.domain.LectureService;
import com.diy.framework.web.beans.factory.ApplicationContext;
import com.diy.framework.web.mvc.servlet.RestController;

import java.util.Arrays;
import java.util.Map;


public class LectureRestController implements RestController {
    private static LectureRestController instance;
    private LectureRestController() {
        ApplicationContext context = ApplicationContext.getInstance();
        this.lectureService = (LectureService) context.getBean("LectureService");
    }
    public static LectureRestController getInstance() {
        if (instance == null) {
            instance = new LectureRestController();
        }
        return instance;
    }
    private final LectureService lectureService;

    @Override
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
