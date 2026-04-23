package com.diy.framework;

import com.diy.app.lecture.LectureController;
import com.diy.app.lecture.LectureRepository;
import com.diy.app.lecture.LectureService;
import com.diy.framework.exception.MethodNotAllowedException;
import com.diy.framework.view.JspViewResolver;
import com.diy.framework.view.ViewResolver;

import javax.servlet.ServletContext;
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

    private final Map<String, Controller> mappings = new HashMap<>();

    @Override
    public void init() {
        ServletContext servletContext = getServletContext();
        ViewResolver viewResolver = new JspViewResolver(servletContext);

        LectureRepository lectureRepository = new LectureRepository();
        LectureService lectureService = new LectureService(lectureRepository);
        LectureController lectureController = new LectureController(lectureService, viewResolver);
                                                             
        mappings.put("/lectures", lectureController);
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();

        Controller controller = mappings.get(requestURI);
        if (controller == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            controller.handleRequest(req, resp);
        } catch (MethodNotAllowedException e) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
