package com.diy.app.business.controller;

import com.diy.app.business.domain.Lecture;
import com.diy.app.business.service.LectureService;
import com.diy.app.infra.dto.ModelAndView;
import com.diy.app.infra.httpSpec.HttpMethod;
import com.diy.app.infra.port.Controller;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class LectureController implements Controller {

    private LectureService service;
    private ObjectMapper objectMapper;

    public LectureController(LectureService service) {
        this.service = service;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String uri = req.getRequestURI();
        String method = req.getMethod();

        if (method.equals(HttpMethod.GET.getValue())) {
            String[] uriArg = uri.split("/");
            if (uriArg.length == 3) {
                long id = Long.parseLong(uriArg[2]);
                Map<String, Object> model = Map.of("lecture", service.getLectureById(id));

                return new ModelAndView("lecture", model);

            } else if (uriArg.length == 2) {
                Map<String, Object> model = Map.of("lectures", service.getAllLectures());
                return new ModelAndView("lecture-list", model);
            }
        }
        if (method.equals(HttpMethod.POST.getValue())) {
            Lecture body = objectMapper.readValue(req.getReader(), Lecture.class);
            service.create(body.getName(), body.getPrice());

            return new ModelAndView("redirect:/lectures");
        }
        if (method.equals(HttpMethod.PUT.getValue())) {
            Lecture body = objectMapper.readValue(req.getReader(), Lecture.class);
            service.update(body);
            return new ModelAndView("redirect:/lectures");
        }
        if (method.equals(HttpMethod.DELETE.getValue())) {
            String[] uriArg = uri.split("/");
            long id = Long.parseLong(uriArg[2]);
            service.delete(id);
            return new ModelAndView("redirect:/lectures");
        }

        throw new IllegalArgumentException("존재하지 않는 메서드");
    }
}
