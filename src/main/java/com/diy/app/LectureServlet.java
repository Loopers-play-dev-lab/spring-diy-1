package com.diy.app;

import javax.servlet.ServletConfig;
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

@WebServlet("/lecture")
public class LectureServlet extends HttpServlet {

    @Override
    public void init(final ServletConfig config) throws ServletException {
        System.out.println("init called.");
        super.init(config);
    }

//    @Override
//    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
//        System.out.println("service called.");
//        super.service(req, resp);
//    }


    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("do get called.");

        List<Map> lectures = new ArrayList<>();
        Map<String, String> lectureMap = new HashMap<>();
        lectureMap.put("id", "A12334");
        lectureMap.put("name", "컴퓨터학개론");
        lectureMap.put("price", "20,000원");
        lectures.add(lectureMap);
        req.setAttribute("lectures", lectures);

        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);


    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("do post called.");
    }
}

