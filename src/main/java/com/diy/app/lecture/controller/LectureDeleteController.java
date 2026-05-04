package com.diy.app.lecture.controller;

import com.diy.app.lecture.service.LectureService;
import com.diy.framework.web.beans.factory.Autowired;
import com.diy.framework.web.beans.factory.Component;
import com.diy.framework.web.Controller;
import com.diy.framework.web.view.ModelAndView;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LectureDeleteController implements Controller {

    private final LectureService lectureService;

    @Autowired
    public LectureDeleteController(final LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @Override
    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        String name = request.getParameter("name");

        try {
            lectureService.delete(name);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return null;
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        } catch (NoSuchElementException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }
}
