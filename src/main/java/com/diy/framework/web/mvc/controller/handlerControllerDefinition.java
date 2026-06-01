package com.diy.framework.web.mvc.controller;

import com.diy.framework.web.mvc.ControllerV1;
import com.diy.framework.web.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InterfaceControllerDefinition implements ControllerDefinition {
    private final ControllerV1 controller;
    private final String url;

    public InterfaceControllerDefinition(String url,ControllerV1 controller) {
        this.controller = controller;
        this.url = url;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.controller.handleRequest(request,response);
    }

    public String getUrl() {
        return url;
    }
}
