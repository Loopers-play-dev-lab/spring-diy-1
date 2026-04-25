package com.diy.app.presentation;

import com.diy.app.domain.Lecture;
import com.diy.config.AppConfig;
import com.diy.framework.web.servlet.AbstractController;
import com.diy.framework.web.view.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.diy.app.presentation.LectureRequestMapper.jsonToLectureRequest;
import static com.diy.app.presentation.RequestReader.readRequest;

public class LectureAbstractController extends AbstractController {
    private final LectureController lectureController;
    private static LectureAbstractController instance;

    private LectureAbstractController() {
        this.lectureController = AppConfig.lectureController();
    }

    public static LectureAbstractController getInstance() {
        if (instance == null) {
            instance = new LectureAbstractController();
        }
        return instance;
    }

    @Override
    public ModelAndView doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        List<Lecture> lectures = lectureController.getLectures();
        req.setAttribute("lectures", lectures);
        return new ModelAndView("lecture-list");
    }

    @Override
    public ModelAndView doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String body = readRequest(req.getReader());
        LectureRequest request = jsonToLectureRequest(body);
        lectureController.addLecture(request);
        return new ModelAndView("redirect:/lectures");
    }

    @Override
    public ModelAndView doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String body = readRequest(req.getReader());
        LectureRequest request = jsonToLectureRequest(body);
        lectureController.updateLecture(request);
        return new ModelAndView("");
    }

    @Override
    public ModelAndView doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String lectureId = req.getParameter("lectureId");
        lectureController.deleteLecture(lectureId);
        return new ModelAndView("");
    }
}
