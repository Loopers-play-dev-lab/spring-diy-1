package com.diy.app.lecture;

import com.diy.app.lecture.request.CreateLectureRequest;
import com.diy.framework.Controller;
import com.diy.framework.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LectureController implements Controller {

    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @Override
    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if ("POST".equals(request.getMethod())) {
            return doPost(request, response);
        } else if ("GET".equals(request.getMethod())) {
            return doGet(request, response);
        }

        throw new RuntimeException("404 Not Found");
    }

    private ModelAndView doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        Map<String, ?> params = (Map<String, ?>) request.getAttribute("params");
        String name = (String) params.get("name");
        int price = Integer.parseInt((String) params.get("price"));
        CreateLectureRequest createRequest = new CreateLectureRequest(name, price);

        lectureService.createLecture(createRequest);

        return new ModelAndView("redirect:/lectures");
    }

    private ModelAndView doGet(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        List<Lecture> lectures = lectureService.getAllLectures();

        Map<String, Object> model = new HashMap<>();
        model.put("lectures", lectures);

        return new ModelAndView("lecture-list", model);
    }
}
