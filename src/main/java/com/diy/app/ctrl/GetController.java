package com.diy.app.ctrl;

import com.diy.app.Lecture;
import com.diy.app.repository.ILectureRepository;
import com.diy.app.repository.LectureRepositoryImpl;
import com.diy.app.view.JspView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class GetController implements Controller {

    private final ILectureRepository repository = new LectureRepositoryImpl();

    @Override
    public String handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("lectures", repository.findAll());
        return "lecture-list";
    }
}
