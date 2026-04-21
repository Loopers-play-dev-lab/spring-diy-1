package com.diy.app;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@WebServlet("/lectures")
public class LectureServlet  extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Lecture> lectures = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        System.out.println("[INIT] servlet initialized");

        IntStream.rangeClosed(1, 5)
                .forEach(i -> lectures.add(new Lecture(i, "TEST"+i, BigDecimal.valueOf(i*10000))));

        super.init();
    }

    /**
     * 강의 목록 조회
      */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[GET] lecture list");

        req.setAttribute("lectures", lectures);

        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
    }

    /**
     * 강의 등록
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("[POST] lecture register");

        Map<String, Object> payload = objectMapper.readValue(req.getReader(), Map.class);
        Lecture lecture = new Lecture(
                lectures.size()+1,
                payload.get("name").toString(),
                new BigDecimal(payload.get("price").toString())
        );
        lectures.add(lecture);

        resp.sendRedirect("/lectures");
    }

    public static class Lecture {
        private final int id;
        private final String name;
        private final BigDecimal price;

        private Lecture(int id, String name, BigDecimal price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public BigDecimal getPrice() {
            return price;
        }
    }
}
