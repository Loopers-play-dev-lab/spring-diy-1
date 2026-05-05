package com.diy.framework.web;

import com.diy.app.controller.HomeGetControllerV1;
import com.diy.app.controller.lecutre.LectureDelControllerV1;
import com.diy.app.controller.lecutre.LectureGetControllerV1;
import com.diy.app.controller.lecutre.LecturePostControllerV1;
import com.diy.app.controller.lecutre.LecturePutControllerV1;
import com.diy.framework.web.beans.annotation.Component;
import com.diy.framework.web.beans.factory.BeanFactory;
import com.diy.framework.web.beans.factory.BeanScanner;
import com.diy.framework.web.utils.*;
import com.diy.framework.web.view.ViewV1;
import com.diy.framework.web.view.ViewResolve;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DispatcherServlet{
    private final Map<String, ControllerV1> getControllerMap = new HashMap<>();
    private final Map<String, ControllerV1> postControllerMap = new HashMap<>();
    private final Map<String, ControllerV1> putControllerMap = new HashMap<>();
    private final Map<String, ControllerV1> delControllerMap = new HashMap<>();
    private final ViewResolve viewResolve;
    private final BeanFactory beanFactory;

    public DispatcherServlet() {
        this.viewResolve = new ViewResolve();
        this.beanFactory = new BeanFactory("com.diy.app");
    }

    public void setPostController(String path, ControllerV1 controller) {
        this.postControllerMap.put(path, controller);
    }

    public void setPutController(String path, ControllerV1 controller) {
        this.putControllerMap.put(path, controller);
    }

    public void setDelControllerMap(String path, ControllerV1 controller) {
        this.delControllerMap.put(path, controller);
    }

    public void setGetControllerMap(String path, ControllerV1 controller) {
        this.getControllerMap.put(path, controller);
    }

    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            RequestBodyV1 body = RequestParam.parseBody(req);
            String method = req.getMethod();
            String uri = req.getRequestURI();
            System.out.println(method + " " + uri);

            ControllerV1 controller = switch (method) {
                case "GET" -> this.getControllerMap.get(uri);
                case "POST" -> this.postControllerMap.get(uri);
                case "PUT" -> this.putControllerMap.get(uri);
                case "DELETE" -> this.delControllerMap.get(uri);
                default -> null;
            };
            if(controller == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            ResponseV1 model = controller.handle(body);
            ViewV1 view = this.viewResolve.getView(req, model.getViewName());
            view.render(req, resp, model);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
