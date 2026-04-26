package com.diy.app.controller;

import com.diy.framework.web.ModelAndView;
import com.diy.framework.web.controller.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LectureController implements Controller {
    @Override
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {

        switch (req.getMethod()) {
            case "POST" -> {
                return doPost(req, res);
            }
            case "GET" -> {
                return doGet(req, res);
            }
            default -> {
                throw new RuntimeException("404 Not Found");
            }
        }

    }

    private ModelAndView doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        return new ModelAndView("redirect:/lectures");
    }

    private ModelAndView doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

//        return new ModelAndView("lecture-list", model);
        return null;
    }
}
