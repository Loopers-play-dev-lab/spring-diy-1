package com.diy.app.controller;

import com.diy.app.controller.lecutre.LectureDelControllerV1;
import com.diy.app.controller.lecutre.LectureGetControllerV1;
import com.diy.app.controller.lecutre.LecturePostControllerV1;
import com.diy.app.controller.lecutre.LecturePutControllerV1;
import com.diy.framework.web.DispatcherServlet;
import com.diy.framework.web.beans.annotation.Component;
import com.diy.framework.web.beans.factory.BeanFactory;
import com.diy.framework.web.beans.factory.BeanScanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebServlet("/")
public class MainController extends HttpServlet {
    DispatcherServlet dispatcherServlet;

    public MainController() {
        this.dispatcherServlet = new DispatcherServlet();

        this.dispatcherServlet.setGetControllerMap("/lectures", new LectureGetControllerV1());
        this.dispatcherServlet.setPostController("/lectures", new LecturePostControllerV1());
        this.dispatcherServlet.setPutController("/lectures", new LecturePutControllerV1());
        this.dispatcherServlet.setDelControllerMap("/lectures", new LectureDelControllerV1());
        this.dispatcherServlet.setGetControllerMap("/", new HomeGetControllerV1());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Start DispatcherServlet");
        BeanFactory beanFactory = new BeanFactory("com.diy.app");
        this.dispatcherServlet.service(req, resp);
    }
}
