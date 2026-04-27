package com.diy.app;

import com.diy.app.lecture.controller.LectureCreateController;
import com.diy.app.lecture.controller.LectureDeleteController;
import com.diy.app.lecture.controller.LectureListController;
import com.diy.app.lecture.controller.LectureUpdateController;
import com.diy.framework.web.Controller;
import com.diy.framework.web.HandlerMapping;
import com.diy.framework.web.view.JspViewResolver;
import com.diy.framework.web.view.ModelAndView;
import com.diy.framework.web.view.View;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private HandlerMapping handlerMapping;
    private final JspViewResolver viewResolver = new JspViewResolver();

    @Override
    public void init() {
        handlerMapping = new HandlerMapping();
        handlerMapping.setMapping("GET", "/lectures", new LectureListController());
        handlerMapping.setMapping("POST", "/lectures", new LectureCreateController());
        handlerMapping.setMapping("PUT", "/lectures", new LectureUpdateController());
        handlerMapping.setMapping("DELETE", "/lectures", new LectureDeleteController());
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException {
        try {
            Controller controller = handlerMapping.getController(req);
            if (controller == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            final ModelAndView mav = controller.handleRequest(req, resp);
            if (mav != null) {
                render(mav, req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void render(final ModelAndView mav, final HttpServletRequest req, final HttpServletResponse resp)
            throws Exception {
        final View view = viewResolver.resolveViewName(mav.getViewName());
        if (view == null) {
            throw new RuntimeException("View not found!!!!!!: " + mav.getViewName());
        }
        view.render(mav.getModel(), req, resp);
    }
}
