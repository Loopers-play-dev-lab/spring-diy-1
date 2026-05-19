package com.diy.app.lecture;

import com.diy.framework.web.mvc.Controller;
import com.diy.framework.web.mvc.view.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
        if ("GET".equals(request.getMethod())) {
            return new ModelAndView("home");
        }

        throw new RuntimeException("404 Not Found");
    }
}
