package com.diy.app.servlet;

import com.diy.framework.web.ModelAndView;
import com.diy.framework.web.controller.Controller;
import com.diy.framework.web.controller.ControllerMap;
import com.diy.framework.web.view.View;
import com.diy.framework.web.viewresolver.JspViewResolver;
import com.diy.framework.web.viewresolver.ViewResolver;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

// "/*" 는 .jsp 나 .html 과 같은 페이지까지 전부 불러오게 됨 -> 내부 동작에 의한 무한 호출에 빠질 수 있음
@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private final ViewResolver viewResolver = new JspViewResolver();
    private ControllerMap controllerMap;

    // 🔥 WebServlet 안에서는 Constructor 대신 init을 써야 함 - 아니면 예외(NoSuchMethodsException) 발생
    @Override
    public void init(ServletConfig config) throws ServletException {
        controllerMap = new ControllerMap();
        super.init(config);
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final Map<String, ?> params = parseParams(req);

        System.out.println(req.getServletPath());

        Controller controller = controllerMap.find(req.getServletPath());

        try {
            final ModelAndView mav = controller.handleRequest(req, resp);
            render(mav, req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    private void render(final ModelAndView mav, final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        final String viewName = mav.getViewName();

        final View view = viewResolver.resolveViewName(viewName);

        if (view == null) {
            throw new RuntimeException("View not found: " + viewName);
        }

        view.render(mav.getModel(), req, resp);
    }

}
