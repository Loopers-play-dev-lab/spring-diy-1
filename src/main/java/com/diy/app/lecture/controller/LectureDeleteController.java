package com.diy.app.lecture.controller;

import com.diy.app.lecture.LectureStorage;
import com.diy.framework.web.Controller;
import com.diy.framework.web.view.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LectureDeleteController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String idStr = request.getParameter("id");

        if (idStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        Long id = Long.parseLong(idStr);
        boolean removed = LectureStorage.LECTURES.removeIf(l -> l.getId().equals(id));

        if (!removed) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        return null;
    }
}
