package com.diy.app.infra.servlet;

import com.diy.app.business.controller.LectureController;
import com.diy.app.business.service.LectureService;
import com.diy.app.infra.dto.ModelAndView;
import com.diy.app.infra.httpSpec.ContentType;
import com.diy.app.infra.httpSpec.HttpHeader;
import com.diy.app.infra.port.Controller;
import com.diy.app.infra.viewRender.ViewResolver;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private final ViewResolver viewResolver = ViewResolver.getInstance();
    private final UrlControllerMapper mapper = UrlControllerMapper.getInstance();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();

        if (uri.equals("/favicon.ico")) return;

        Controller controller = mapper.findController(uri);

        try {
            ModelAndView modelAndView;
            if (Objects.isNull(controller)) modelAndView = new ModelAndView(uri);
            else modelAndView = controller.handleRequest(req, resp);
            viewResolver.resolve(req, resp, modelAndView);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
