package com.diy.app;

import com.diy.app.dto.LectureCreateRequest;
import com.diy.app.dto.LectureUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.NoSuchElementException;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {
    private final LectureRepository repository = LectureRepository.getInstance();
    private final ObjectMapper om = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("lecture-list.jsp");
        Collection<Lecture> lectures = repository.findAll();
        req.setAttribute("lectures", lectures);
        dispatcher.forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            LectureCreateRequest createRequest = om.readValue(req.getReader(), LectureCreateRequest.class);
            System.out.printf("LectureCreateRequest = " + createRequest.toString());
            repository.save(createRequest.toLecture());
            resp.sendRedirect(req.getContextPath() + "/lectures");
        } catch (NumberFormatException | NoSuchElementException e) {
            System.out.printf(e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            LectureUpdateRequest updateRequest = om.readValue(req.getReader(), LectureUpdateRequest.class);
            System.out.printf("LectureUpdateRequest = " + updateRequest.toString());

            Lecture lecture = repository.update(updateRequest.toLecture())
                    .orElseThrow(() -> new NoSuchElementException("강의 ID " + updateRequest.getId() + " 를 찾을 수 없습니다."));
            System.out.printf("강의 ID : " + updateRequest.getId() + " 가 수정되었습니다. " + lecture.toString());

            resp.sendRedirect(req.getContextPath() + "/lectures");
        } catch (NumberFormatException | NoSuchElementException e) {
            System.out.printf(e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            Long id = parseId(req.getParameter("id"));

            Lecture lecture = repository.delete(id)
                    .orElseThrow(() -> new NoSuchElementException("강의 ID " + id + " 를 찾을 수 없습니다."));
            System.out.printf("강의 ID : " + id + " 삭제 완료. " + lecture.toString());

            resp.sendRedirect(req.getContextPath() + "/lectures");
        } catch (NumberFormatException | NoSuchElementException e) {
            System.out.printf(e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private Long parseId(String value){
        try {
            if (value == null || value.isBlank()) throw new NoSuchElementException("ID 는 필수값 입니다.");
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e){
            e.printStackTrace();
            throw new NumberFormatException("ID 형식이 올바르지 않습니다.");
        }
    }
}
