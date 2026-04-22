package com.diy.app.presentation;

import com.diy.config.AppConfig;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/lecture")
public class LectureFrontServlet extends HttpServlet {
    private final LectureController lectureController;

    public LectureFrontServlet() {
        super();
        this.lectureController = AppConfig.lectureController();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("LectureFrontServlet init");
        super.init(config);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LectureFrontServlet service");
        String id = req.getParameter("lectureId");
        System.out.println(id);
        if (id != null) {
            LectureResponse response = lectureController.getLecture(id);
            req.setAttribute("lectureId", response.lectureId());
            req.setAttribute("name", response.name());
            req.setAttribute("price", response.price());
        }
        final RequestDispatcher dispatcher = req.getRequestDispatcher("/lecture-registration.jsp");
        dispatcher.forward(req, resp);
    }
}
