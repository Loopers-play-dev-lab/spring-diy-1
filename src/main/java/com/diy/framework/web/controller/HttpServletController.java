package com.diy.framework.web.controller;

import com.diy.framework.web.HttpRequestMethod;
import com.diy.framework.web.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpServletController {

    private final Controller controller;

    public HttpServletController(Controller controller) {
        this.controller = controller;
    }

    public ModelAndView handleRequest(final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        return controller.handleRequest(HttpRequestMethod.from(req.getMethod()), req, resp);
    }
}
