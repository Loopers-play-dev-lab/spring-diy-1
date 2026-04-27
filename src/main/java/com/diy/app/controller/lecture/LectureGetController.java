package com.diy.app.controller.lecture;

import com.diy.app.controller.AbstractController;
import com.diy.app.repository.LectureRepository;
import com.diy.app.servlet.dto.response.LectureResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class LectureGetController implements AbstractController {
    private  final LectureRepository lectureRepository;

    public LectureGetController(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final RequestDispatcher dispatcher = request.getRequestDispatcher("lecture-list.jsp");
        List<LectureResponse> lectures = lectureRepository.find().stream().map(LectureResponse::from).toList();
        request.setAttribute("lectures", lectures);
        dispatcher.forward(request, response);
    }
}
