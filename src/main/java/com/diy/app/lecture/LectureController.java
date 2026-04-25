package com.diy.app.lecture;

import com.diy.app.lecture.request.CreateLectureRequest;
import com.diy.framework.Controller;
import com.diy.framework.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LectureController implements Controller {

    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response, Map<String, ?> params) throws Exception {
        String method = request.getMethod();
        if ("GET".equals(method)) {
            return doGet();
        } else if ("POST".equals(method)) {
            return doPost(request.getParameterMap());
        }

        return null;
    }

    public ModelAndView doGet() {
        List<Lecture> lectures = lectureService.getAllLectures();

        Map<String, Object> model = new HashMap<>();
        model.put("lectures", lectures);

        return new ModelAndView("lecture-list", model);
    }

    public ModelAndView doPost(Map<String, ?> params) {
        String name = (String) params.get("name");
        int price = Integer.parseInt((String) params.get("price"));
        CreateLectureRequest createRequest = new CreateLectureRequest(name, price);

        lectureService.createLecture(createRequest);

        return new ModelAndView("redirect:/lectures");
    }
}
