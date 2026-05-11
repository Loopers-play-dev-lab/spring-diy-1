package com.diy.framework.web.servlet;

import com.diy.framework.web.utils.*;
import com.diy.framework.web.view.ViewResolve;
import com.diy.framework.web.view.ViewV1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private final Map<String, ControllerV2> controllerMap;
    private final ViewResolve viewResolve;

    public DispatcherServlet(Map<String, ControllerV2> controllerMap) {
        this.controllerMap = controllerMap;
        this.viewResolve = new ViewResolve();
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("start : " + req.getRequestURI());
            RequestBodyV1 body = RequestParam.parseBody(req);
            String method = req.getMethod();
            String uri = req.getRequestURI();
            System.out.println(method + " " + uri);

            ControllerV2 controller = this.controllerMap.get(uri);

            ResponseV1 model = controller.handle(method, body);
            ViewV1 view = this.viewResolve.getView(req, model.getViewName());
            view.render(req, resp, model);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
