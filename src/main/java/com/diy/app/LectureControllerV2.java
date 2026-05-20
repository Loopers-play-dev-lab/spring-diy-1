package com.diy.app;

import com.diy.framework.context.annotation.Autowired;
import com.diy.framework.context.annotation.Controller;
import com.diy.framework.context.annotation.RequestMapping;
import com.diy.framework.context.annotation.RequestMethod;
import com.diy.framework.web.mvc.ControllerV1;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/lectures/v2")
@Controller
public class LectureControllerV2 {

    private final LectureService lectureService;

    @Autowired
    public LectureControllerV2(final LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @RequestMapping(methods = RequestMethod.POST)
    public ModelAndView doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final byte[] bodyBytes = req.getInputStream().readAllBytes();
        final String body = new String(bodyBytes, StandardCharsets.UTF_8);


        final Lecture lecture = new ObjectMapper().readValue(body, Lecture.class);

        return new ModelAndView("redirect:/lectures");
    }

    @RequestMapping(methods = RequestMethod.GET)
    public ModelAndView doGet(final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        final Map<String, Object> model = new HashMap<>();
//        model.put("lectures", lectureModels);

        return new ModelAndView("lecture-list", model);
    }
}
