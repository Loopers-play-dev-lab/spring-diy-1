package com.diy.app;

import com.diy.framework.context.annotation.Controller;
import com.diy.framework.context.annotation.RequestMapping;
import com.diy.framework.web.mvc.RequestMethod;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/lectures")
public class LectureController {

    private final Map<Long, Lecture> lectureRepository = new HashMap<>();

    @RequestMapping(methods = RequestMethod.GET)
    public ModelAndView list(final HttpServletRequest req, final HttpServletResponse resp) {
        final Collection<Lecture> lectures = lectureRepository.values();
        final Object lectureModels = lectures.stream()
                .map(lecture -> Map.of("id", lecture.getId(), "name", lecture.getName(), "price", lecture.getPrice()))
                .toList();

        final Map<String, Object> model = new HashMap<>();
        model.put("lectures", lectureModels);

        return new ModelAndView("lecture-list", model);
    }

    @RequestMapping(methods = RequestMethod.POST)
    public ModelAndView create(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final byte[] bodyBytes = req.getInputStream().readAllBytes();
        final String body = new String(bodyBytes, StandardCharsets.UTF_8);

        final Lecture lecture = new ObjectMapper().readValue(body, Lecture.class);

        final long id = lectureRepository.size();
        lectureRepository.put(id, lecture);
        lecture.setId(id);

        return new ModelAndView("redirect:/lectures");
    }
}
