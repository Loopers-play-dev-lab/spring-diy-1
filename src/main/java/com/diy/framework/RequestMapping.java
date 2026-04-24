package com.diy.framework;

import com.diy.app.lecture.LectureController;
import com.diy.app.lecture.LectureRepository;
import com.diy.app.lecture.LectureService;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {

    private final Map<HandlerKey, Controller> mappings = new HashMap<>();

    public void initMapping() {
        LectureRepository lectureRepository = new LectureRepository();
        LectureService lectureService = new LectureService(lectureRepository);
        LectureController lectureController = new LectureController(lectureService);

        mappings.put(new HandlerKey("GET", "/lectures"), lectureController::doGet);
        mappings.put(new HandlerKey("POST", "/lectures"), lectureController::doPost);
    }

    public Controller getController(HandlerKey key) {
        return mappings.get(key);
    }
}
