package com.diy.app;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@WebServlet("/lectures")
public class LectureServlet  extends HttpServlet {

    @Override
    public void init() throws ServletException {
        System.out.println("[INIT] servlet initialized");
        super.init();
    }

    /**
     * 강의 목록
      */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[GET] lecture list");

        List<Map<String, Object>> lectures = new ArrayList<>();
        IntStream.rangeClosed(1, 5).forEach(i -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", i);
            map.put("name", "TEST" + i);
            map.put("price", i * 10000);
            lectures.add(map);
        });
        req.setAttribute("lectures", lectures);

        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
    }
}
