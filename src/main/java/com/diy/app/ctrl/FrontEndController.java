package com.diy.app.ctrl;

import com.diy.app.Main;
import com.diy.app.utils.ApplicationContext;
import com.diy.app.utils.HandlerExecution;
import com.diy.app.utils.HandlerMapping;
import com.diy.app.view.ModelAndView;
import com.diy.app.view.View;
import com.diy.app.view.ViewResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/")
public class FrontEndController extends HttpServlet {

    private HandlerMapping handlerMapping;
    private ViewResolver viewResolver;
    @Override
    public void init() throws ServletException {
        // Bean 초기화 작업
        ApplicationContext.run(Main.class);

        // Strategy 전략을 통해 html로 변경시 suffix, prefix만 설정
        viewResolver = new ViewResolver("/",".jsp");
        handlerMapping = ApplicationContext.getBean(HandlerMapping.class);
        handlerMapping.initalize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String method = req.getMethod();
        String uri = req.getRequestURI();
        HandlerExecution handler =  handlerMapping.getHandler(method, uri);
        if (handler == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            ModelAndView mav = handler.handle(req, resp);
            String viewName = mav.getViewName();

            if (viewName.startsWith("redirect:")) {
                String path = viewName.substring("redirect:".length());
                resp.sendRedirect(req.getContextPath() + path);
                return;
            }

            View view = viewResolver.resolve(viewName);

            if (view != null) {
                view.render(mav.getModel(), req, resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
