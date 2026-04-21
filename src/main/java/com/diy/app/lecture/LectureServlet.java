package com.diy.app.lecture;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {
    private final LectureRepository repository = new LectureRepositoryImpl();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() {
        repository.save(new Lecture(0L, "Java Servlet", 10000));
        repository.save(new Lecture(0L, "Spring MVC", 20000));
        repository.save(new Lecture(0L, "JPA Basic", 30000));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String idParam = req.getParameter("id");

        if (idParam == null || idParam.isBlank()) {
            req.setAttribute("lectures", repository.findAll().values());
            req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
            return;
        }

        // TODO("lecture 단일 상세조회 예비용)"
        try {
            final long id = Long.parseLong(idParam);
            final Lecture lecture = repository.findById(id);

            req.setAttribute("lecture", lecture);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "id는 숫자여야 합니다.");
        } catch (LectureException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Lecture lecture = parseRequestJsonToLecture(req);
        repository.save(lecture);
        resp.sendRedirect("/lectures");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Lecture lecture = parseRequestJsonToLecture(req);
        repository.update(lecture);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Lecture lecture = parseRequestJsonToLecture(req);
        repository.delete(lecture);
    }

    private Lecture parseRequestJsonToLecture(HttpServletRequest req) throws IOException {
        req.setCharacterEncoding("UTF-8");
        return mapper.readValue(req.getReader(), Lecture.class);
    }
}
