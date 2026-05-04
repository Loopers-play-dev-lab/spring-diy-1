package com.diy.app;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    public void init() throws ServletException{
        System.out.println(">>> init 호출됨 ! 나 지금 만들어지는 중~~~~");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException{
        System.out.println(">>> doGet 호출됨! URL=" + req.getRequestURI());
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("Hello, Servlet!");
    }

    @Override
    public void destroy() {
        System.out.println(">>> destroy 호출됨! (나 지금 소멸 중)");
    }
}
