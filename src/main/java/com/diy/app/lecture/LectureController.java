package com.diy.app.lecture;

import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.mvc.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class LectureController implements Controller {

    private final LectureDao lectureDao = new LectureDao();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        switch (request.getMethod()) {
            case "GET":
                return doGet();
            case "POST":
                return doPost(request);
            case "PUT":
                return doPut(request, response);
            case "DELETE":
                return doDelete(request, response);
            default:
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                return null;
        }
    }

    private ModelAndView doGet() {
        final List<Lecture> lectures = lectureDao.findAll();
        return new ModelAndView("lecture-list").addAttribute("lectures", lectures);
    }

    private ModelAndView doPost(HttpServletRequest request) throws Exception {
        final Lecture body = objectMapper.readValue(request.getInputStream(), Lecture.class);
        lectureDao.save(body);
        return new ModelAndView("redirect:/lectures");
    }

    private ModelAndView doPut(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Lecture body = objectMapper.readValue(request.getInputStream(), Lecture.class);
        try {
            lectureDao.update(body);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return null;
    }

    private ModelAndView doDelete(HttpServletRequest request, HttpServletResponse response) {
        final String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
        lectureDao.deleteById(Long.parseLong(idParam));
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        return null;
    }
}
