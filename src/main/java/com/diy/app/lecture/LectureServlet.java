package com.diy.app.lecture;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/lecture", "/lectures"})
public class LectureServlet extends HttpServlet {
    // 의존성 주입 전 객체 직접 생성
    private final LectureRepository repository = new LectureRepositoryImpl();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() {
        // 서버 시작시 초기 데이터 삽입
        repository.save(new Lecture(0L, "Java Servlet", 10000));
        repository.save(new Lecture(1L, "Spring MVC", 20000));
        repository.save(new Lecture(2L, "JPA Basic", 30000));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String idParam = req.getParameter("id");

        // request 서블릿 경로 따라 분기
        if ("/lectures".equals(req.getServletPath())) {
            req.setAttribute("lectures", repository.findAll().values());
            req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
            return;
        }

        try {
            if (idParam == null || idParam.isBlank()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "id가 필요합니다.");
                return;
            }

            final long id = Long.parseLong(idParam);
            final Lecture lecture = repository.findById(id);

            req.setAttribute("lecture", lecture);
            resp.setContentType("application/json;charset=UTF-8");
            mapper.writeValue(resp.getWriter(), lecture);
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
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Lecture lecture = parseRequestJsonToLecture(req);
        repository.delete(lecture);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private Lecture parseRequestJsonToLecture(HttpServletRequest req) throws IOException {
        req.setCharacterEncoding("UTF-8");
        return mapper.readValue(req.getReader(), Lecture.class);
    }
}
