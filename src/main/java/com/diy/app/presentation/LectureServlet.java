package com.diy.app.presentation;

import com.diy.app.domain.Lecture;
import com.diy.config.AppConfig;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.diy.app.presentation.LectureRequestMapper.jsonToLectureRequest;
import static com.diy.app.presentation.RequestReader.readRequest;

@WebServlet(urlPatterns = "/lectures")
public class LectureServlet extends HttpServlet {
    private final LectureController lectureController;

    public LectureServlet() {
        super();
        this.lectureController = AppConfig.lectureController();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        List<Lecture> lectures = lectureController.getLectures();
        req.setAttribute("lectures", lectures);
        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LectureServlet doPost");

        String body = readRequest(req.getReader());
        LectureRequest request = jsonToLectureRequest(body);

        lectureController.addLecture(request);
        resp.sendRedirect("/lectures");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LectureServlet doPut");

        String body = readRequest(req.getReader());
        LectureRequest request = jsonToLectureRequest(body);

        lectureController.updateLecture(request);
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);
        resp.setHeader("Location", "/lectures");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LectureServlet doDelete");
        String lectureId = req.getParameter("lectureId");
        System.out.println(lectureId);

        lectureController.deleteLecture(lectureId);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
