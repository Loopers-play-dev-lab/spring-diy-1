package com.diy.app.servlet;

import com.diy.app.domain.Lecture;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {
    long nextId;
    List<Lecture> lectures;

    @Override
    public void init() throws ServletException {
        nextId = 0;
        lectures = new ArrayList<>();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.getWriter().write(lectures.stream().map(Lecture::toString).collect(Collectors.joining(",", "[", "]")));
        } catch (Exception e) {
            resp.getWriter().write("Error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Lecture lecture = new Lecture(10, "name", 10000);
//        Lecture lecture = new Lecture(nextId++, body.get("name")[0], Long.parseLong(body.get("price")[0]));
        lectures.add(lecture);
        resp.getWriter().write(lecture.toString());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));
        lectures.removeIf(lecture -> lecture.getId() == id);
    }

    private String getBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader bf = null;

        try {
            ServletInputStream input = req.getInputStream();
            if (input != null) {
                bf = new BufferedReader(new InputStreamReader(input));
                char[] buff = new char[128];
                int readLen = -1;
                while (((readLen = bf.read(buff)) > 0)) {
                    sb.append(buff, 0, readLen);
                }
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }

        return sb.toString();
    }
}
