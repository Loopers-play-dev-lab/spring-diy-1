package com.diy.framework.web.mvc.servlet;

import com.diy.framework.web.mvc.view.ModelAndView;

import java.util.Map;

public abstract class AbstractController implements Controller{
    @Override
    public ModelAndView handleRequest(String method, Map<String, ?> params) throws IllegalArgumentException {
        System.out.println("AbstractController handleRequest method : " + method);

        switch (method) {
            case "GET" : return doGet(params);
            case "POST" : return doPost(params);
            case "PUT" : return doPut(params);
            case "DELETE" : return doDelete(params);
            default : throw new IllegalArgumentException("Method not supported");
        }
    }

    public abstract ModelAndView doGet (Map<String, ?> params);
    public abstract ModelAndView doPost (Map<String, ?> params);
    public abstract ModelAndView doPut (Map<String, ?> params);
    public abstract ModelAndView doDelete (Map<String, ?> params);
}
