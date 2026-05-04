package com.diy.app.lecture.controller;

import com.diy.app.lecture.LectureRepository;
import com.diy.framework.web.Controller;
import com.diy.framework.web.view.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LectureDeleteController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name = request.getParameter("name");

        if (name == null || name.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        boolean deleted = LectureRepository.deleteByName(name);
        if (!deleted) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        return null;
    }
}
