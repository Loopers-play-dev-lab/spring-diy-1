package com.diy.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {

    private static final List<Lecture> STORAGE = new ArrayList<>();
    static {
        STORAGE.add(new Lecture(1L, "스프링 DIY", "Jude"));
        STORAGE.add(new Lecture(2L, "리액트 입문", "Anna"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(STORAGE);

        System.out.println(">>> doGet 호출됨! URL=" + req.getRequestURI());

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException{

        byte[] bodyBytes = req.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);
        System.out.println(">>>>>> 받은 body : " + body); //화깅용

        ObjectMapper mapper = new ObjectMapper();
        Lecture lecture = mapper.readValue(body, Lecture.class);

        long nextId = STORAGE.size() + 1;
        lecture.id = nextId;
        STORAGE.add(lecture);
        resp.setStatus(201);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(mapper.writeValueAsString(lecture));  // 새로 생성된 것 반환

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
        throws IOException{

        byte[] bodyBytes = req.getInputStream().readAllBytes();
        String body = new String(bodyBytes, StandardCharsets.UTF_8);
        System.out.println(">>>>>> 받은 body : " + body); //확인용

        ObjectMapper mapper = new ObjectMapper();
        Lecture updated = mapper.readValue(body, Lecture.class);

        Lecture target = STORAGE.stream()
                .filter(l -> l.id.equals(updated.id))
                .findFirst()
                .orElse(null);

        if (target == null) {
            resp.setStatus(404);
            return;
        }
        target.title = updated.title;
        target.instructor = updated.instructor;

        resp.setStatus(200);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(mapper.writeValueAsString(target));

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {

        String idStr = req.getParameter("id");
        System.out.println(">>>>>> DELETE 요청, id=" + idStr);

        if (idStr == null) {
            resp.setStatus(400);
            return;
        }

        Long id = Long.parseLong(idStr);
        boolean removed = STORAGE.removeIf(l -> l.id.equals(id));

        if (!removed) {
            resp.setStatus(404);
            return;
        }

        resp.setStatus(204);
    }

}
