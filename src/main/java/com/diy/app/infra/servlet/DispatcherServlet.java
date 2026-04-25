package com.diy.app.infra.servlet;

import com.diy.app.business.controller.LectureController;
import com.diy.app.business.service.LectureService;
import com.diy.app.infra.httpSpec.ContentType;
import com.diy.app.infra.httpSpec.HttpHeader;
import com.diy.app.infra.port.Controller;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private final UrlControllerMapper mapper = UrlControllerMapper.getInstance();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();

        Controller controller = mapper.findController(uri);
        try {
            controller.handleRequest(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
