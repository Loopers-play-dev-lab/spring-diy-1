package com.diy.app.controller;

import com.diy.app.controller.lecutre.LectureDelControllerV1;
import com.diy.app.controller.lecutre.LectureGetControllerV1;
import com.diy.app.controller.lecutre.LecturePostControllerV1;
import com.diy.app.controller.lecutre.LecturePutControllerV1;
import com.diy.framework.web.ControllerV1;
import com.diy.framework.web.Model;
import com.diy.framework.web.RequestBody;
import com.diy.framework.web.RequestParam;
import com.diy.framework.web.view.View;
import com.diy.framework.web.view.ViewResolve;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private final Map<String, ControllerV1> getControllerMap = new HashMap<>();
    private final Map<String, ControllerV1> postControllerMap = new HashMap<>();
    private final Map<String, ControllerV1> putControllerMap = new HashMap<>();
    private final Map<String, ControllerV1> delControllerMap = new HashMap<>();
    private final ViewResolve viewResolve;

    public DispatcherServlet() {
        this.getControllerMap.put("/lectures", new LectureGetControllerV1());
        this.postControllerMap.put("/lectures", new LecturePostControllerV1());
        this.putControllerMap.put("/lectures", new LecturePutControllerV1());
        this.delControllerMap.put("/lectures", new LectureDelControllerV1());
        this.viewResolve = new ViewResolve();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            RequestBody body = RequestParam.parseBody(req);
            String method = req.getMethod();
            String uri = req.getRequestURI();
            ControllerV1 controller = null;
            switch (method) {
                case "GET":
                    controller = this.getControllerMap.get(uri);
                    break;
                case "POST":
                    controller = this.postControllerMap.get(uri);
                    break;
                case "PUT":
                    controller = this.putControllerMap.get(uri);
                    break;
                case "DELETE":
                    controller = this.delControllerMap.get(uri);
                    break;
            }
            if(controller == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            Model model = controller.handle(body);

            View view = this.viewResolve.getView(req, model.getViewName());
            view.render(req, resp, model);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        super.service(req, resp);
    }
}
