package com.diy.app.ctrl;

import com.diy.app.Lecture;
import com.diy.app.repository.ILectureRepository;
import com.diy.app.repository.LectureRepositoryImpl;
import com.diy.app.view.JspView;
import com.diy.app.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class GetController implements Controller {

    private final ILectureRepository repository = new LectureRepositoryImpl();

    @Override
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        List<Lecture> lectures = repository.findAll();
        ModelAndView mav = new ModelAndView("lecture-list");
        mav.addObject("lectures", lectures);
        return mav;
    }
}
