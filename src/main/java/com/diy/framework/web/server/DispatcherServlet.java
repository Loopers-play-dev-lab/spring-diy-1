package com.diy.framework.web.server;

import com.diy.app.controller.api.LectureServlet;
import com.diy.app.controller.view.LectureListServlet;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
  private Map<String, HttpServlet> servletMap;

  @Override
  public void init() {
    servletMap = new HashMap<>();
    servletMap.put("/lectures", new LectureServlet());
    servletMap.put("/lecture-list", new LectureListServlet());
  }

  @Override
  protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
    String path = getUriPath(req);

    HttpServlet servlet = servletMap.get(path);
    if (servlet == null) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    final Map<String, ?> params = parseParams(req);
    req.setAttribute("params", params);

    servlet.service(req, resp);
  }

  private Map<String, ?> parseParams(final HttpServletRequest req) throws IOException {
    if ("application/json".equals(req.getHeader("Content-Type"))) {
      final byte[] bodyBytes = req.getInputStream().readAllBytes();
      final String body = new String(bodyBytes, StandardCharsets.UTF_8);

      return new ObjectMapper().readValue(body, new TypeReference<Map<String, Object>>() {});
    } else {
      return req.getParameterMap();
    }
  }

  private String getUriPath(final HttpServletRequest req) {
    final String originPath = req.getRequestURI();
    final String trimPath = originPath.replace("%20", "");
    if (trimPath.endsWith("/")) return trimPath.substring(0, trimPath.length() - 1);
    return trimPath;
  }

}
