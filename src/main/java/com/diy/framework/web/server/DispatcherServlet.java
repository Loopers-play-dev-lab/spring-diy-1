package com.diy.framework.web.server;

import com.diy.app.controller.lecture.LectureController;
import com.diy.app.repository.LectureRepository;
import com.diy.framework.web.bean.BeanContainer;
import com.diy.framework.web.controller.AbstractController;
import com.diy.framework.web.view.ModelAndView;
import com.diy.framework.web.view.View;
import com.diy.framework.web.view.ViewResolver;
import com.diy.framework.web.view.resolver.HtmlViewResolver;
import com.diy.framework.web.view.resolver.JspViewResolver;
import com.diy.framework.web.view.resolver.RedirectViewResolver;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private HandlerMapping handlerMapping;
    private List<ViewResolver> viewResolvers;
    private final BeanContainer container = new BeanContainer();

    @Override
    public void init() throws ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        viewResolvers = List.of(new JspViewResolver(), new HtmlViewResolver(), new RedirectViewResolver());
        this.handlerMapping = new HandlerMapping(
                Map.of(
                        "/lectures", new LectureController(objectMapper, container.getBean(LectureRepository.class))
                )
        );
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        String path = req.getRequestURI();
        AbstractController controller = handlerMapping.getController(path);

        try {
            ModelAndView modelAndView = controller.handleRequest(req, resp);
            render(modelAndView, req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void render(final ModelAndView mav, final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        final String viewName = mav.getViewName();

        final View view = resolveView(viewName);

        if (view == null) {
            throw new RuntimeException("View not found: " + viewName);
        }

        view.render(mav.getModel(), req, resp);
    }

    private View resolveView(String viewName) {
        return viewResolvers
                .stream()
                .filter(v -> v.match(viewName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("no match view"))
                .resolveViewName(viewName);
    }
}
