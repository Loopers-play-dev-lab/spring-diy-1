package com.diy.app;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        System.out.println("[LectureServlet] init() is called.");
        super.init();
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[LectureServlet] service() is called.");
        super.service(req, resp);
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[LectureServlet] doGet() is called.");
        final Lecture loopakBeL2 = Lecture.of(1L, "Loop:PAK BE L2", BigDecimal.valueOf(1300000));
        final Lecture springDiy = Lecture.of(2L, "Spring DIY", BigDecimal.valueOf(99000));
        req.setAttribute("lectures", List.of(loopakBeL2, springDiy));
        req.getRequestDispatcher("/lecture-list.jsp").forward(req, resp);
    }

    @Override
    public void destroy() {
        System.out.println("[LectureServlet] destroy() is called.");
        super.destroy();
    }
}

