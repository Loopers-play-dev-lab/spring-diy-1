package com.diy.app.lecture;

import com.diy.app.lecture.request.CreateLectureRequest;
import com.diy.framework.context.annotation.Controller;
import com.diy.framework.web.mvc.annotation.RequestMapping;
import com.diy.framework.web.mvc.annotation.RequestMethod;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @RequestMapping(methods = RequestMethod.POST)
    public ModelAndView doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        byte[] bodyBytes = request.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);
        CreateLectureRequest createRequest = new ObjectMapper().readValue(body, CreateLectureRequest.class);

        lectureService.createLecture(createRequest);

        return new ModelAndView("redirect:/lectures");
    }

    @RequestMapping(methods = RequestMethod.GET)
    public ModelAndView doGet(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        List<Lecture> lectures = lectureService.getAllLectures();

        Map<String, Object> model = new HashMap<>();
        model.put("lectures", lectures);

        return new ModelAndView("lecture-list", model);
    }
}
