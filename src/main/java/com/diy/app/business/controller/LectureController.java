package com.diy.app.business.controller;

import com.diy.app.business.domain.Lecture;
import com.diy.app.business.service.LectureService;
import com.diy.app.infra.httpSpec.HttpMethod;
import com.diy.app.infra.port.Controller;
import com.diy.app.infra.viewRender.HtmlView;
import com.diy.app.infra.viewRender.JspView;
import com.diy.app.infra.viewRender.ViewResolver;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LectureController implements Controller {
    public static final String PREFIX_URL = "/lecture";

    private ViewResolver viewResolver;
    private LectureService service;
    private ObjectMapper objectMapper;

    public LectureController(ViewResolver viewResolver, LectureService service) {
        this.viewResolver = viewResolver;
        this.service = service;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String uri = req.getRequestURI();
        String method = req.getMethod();

        if (method.equals(HttpMethod.GET.getValue())) {
            String[] uriArg = uri.split("/");
            if (uriArg.length == 3) {
                long id = Long.parseLong(uriArg[2]);
                req.setAttribute("lecture", service.getLectureById(id));
            } else if (uriArg.length == 2) {
                req.setAttribute("lectures", service.getAllLectures());
                viewResolver.resolve(req, resp, "lecture-list");
            }
        }
        if (method.equals(HttpMethod.POST.getValue())) {
            Lecture body = objectMapper.readValue(req.getReader(), Lecture.class);
            service.create(body.getName(), body.getPrice());

            resp.sendRedirect("/lectures");
        }
        if (method.equals(HttpMethod.PUT.getValue())) {
            Lecture body = objectMapper.readValue(req.getReader(), Lecture.class);
            service.update(body);
        }
        if (method.equals(HttpMethod.DELETE.getValue())) {
            String[] uriArg = uri.split("/");
            long id = Long.parseLong(uriArg[2]);
            req.setAttribute("lecture", service.getLectureById(id));
        }
    }
}
