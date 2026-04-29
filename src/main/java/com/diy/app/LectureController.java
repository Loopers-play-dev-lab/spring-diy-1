package com.diy.app;

import com.diy.framework.web.mvc.ModelAndView;
import com.diy.framework.web.mvc.RequestMapping;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LectureController {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final Map<Long, Lecture> repository = new HashMap<>();

    @RequestMapping(value = "/lectures", method = "GET")
    public ModelAndView list(final HttpServletRequest req, final HttpServletResponse resp) {
        final Collection<Lecture> lectures = repository.values();
        final Map<String, Object> model = new HashMap<>();
        model.put("lectures", lectures);
        return new ModelAndView("lecture-list", model);
    }

    @RequestMapping(value = "/lectures", method = "POST")
    public ModelAndView create(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final String body = new String(req.getInputStream().readAllBytes());
        final Lecture lecture = OBJECT_MAPPER.readValue(body, Lecture.class);

        final long id = repository.size() + 1L;
        lecture.setId(id);
        repository.put(lecture.getId(), lecture);

        return new ModelAndView("redirect:/lectures");
    }

    @RequestMapping(value = "/lectures", method = "PUT")
    public ModelAndView update(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final String body = new String(req.getInputStream().readAllBytes());
        final Lecture lecture = OBJECT_MAPPER.readValue(body, Lecture.class);

        repository.put(lecture.getId(), lecture);

        return new ModelAndView("redirect:/lectures");
    }

    @RequestMapping(value = "/lectures", method = "DELETE")
    public ModelAndView delete(final HttpServletRequest req, final HttpServletResponse resp) {
        final Long id = Long.valueOf(req.getParameter("id"));
        repository.remove(id);

        return new ModelAndView("redirect:/lectures");
    }
}
