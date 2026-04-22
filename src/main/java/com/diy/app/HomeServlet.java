package com.diy.app;

import com.diy.app.entity.Lecture;
import com.diy.app.dto.CreateLectureRequest;
import com.diy.app.dto.ModifyLectureRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/lectures")
public class HomeServlet extends HttpServlet {
    private final Map<Long, Lecture> lectureRepository = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final RequestDispatcher requestDispatcher = req.getRequestDispatcher("lecture-list.jsp");
        final Collection<Lecture> lectures = lectureRepository.values();

        req.setAttribute("lectures", lectures);
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final CreateLectureRequest dto = mapper.readValue(req.getReader(), CreateLectureRequest.class);
        final Lecture lecture = dto.toEntity();

        lectureRepository.put(lecture.getId(), lecture);
        resp.sendRedirect(req.getContextPath() + "/lectures");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final ModifyLectureRequest dto = mapper.readValue(req.getReader(), ModifyLectureRequest.class);

        if (!lectureRepository.containsKey(dto.getId())) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } else {
            lectureRepository.put(dto.getId(), dto.toEntity());
        }

        resp.sendRedirect(req.getContextPath() + "/lectures");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Long id = Long.parseLong(req.getParameter("id"));

        if (!lectureRepository.containsKey(id)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } else {
            lectureRepository.remove(id);
        }

        resp.sendRedirect(req.getContextPath() + "/lectures");
    }
}
