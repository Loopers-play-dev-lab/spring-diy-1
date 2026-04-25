package com.diy.framework.web.servlet;

import com.diy.framework.web.view.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractController implements Controller{
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = request.getMethod();
        System.out.println("AbstractController handleRequest method : " + method);

        switch (method) {
            case "GET" : return doGet(request, response);
            case "POST" : return doPost(request, response);
            case "PUT" : return doPut(request, response);
            case "DELETE" : return doDelete(request, response);
            default : throw new Exception("Method not supported");
        }
    }

    public abstract ModelAndView doGet (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
    public abstract ModelAndView doPost (HttpServletRequest request, HttpServletResponse response) throws IOException;
    public abstract ModelAndView doPut (HttpServletRequest request, HttpServletResponse response) throws IOException;
    public abstract ModelAndView doDelete (HttpServletRequest request, HttpServletResponse response);
}
