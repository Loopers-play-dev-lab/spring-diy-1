package com.diy.app.lecture.controller;

import com.diy.app.lecture.LectureStorage;
import com.diy.framework.web.Controller;
import com.diy.framework.web.view.Model;
import com.diy.framework.web.view.View;
import com.diy.framework.web.view.ViewResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LectureListController implements Controller {

    private final ViewResolver viewResolver = new ViewResolver("/", ".jsp"); //jsp로
    //    private final ViewResolver viewResolver = new ViewResolver("/", ".html");

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Model model = new Model();
        model.setAttribute("lectures", LectureStorage.LECTURES);

        View view = viewResolver.resolve("lecture-list");
        view.render(model, request, response);
    }
}
