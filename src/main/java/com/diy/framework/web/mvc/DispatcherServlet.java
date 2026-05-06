package com.diy.framework.web.mvc;

import com.diy.app.LectureController;
import com.diy.framework.web.beans.Component;
import com.diy.framework.web.beans.factory.BeanFactory;
import com.diy.framework.web.beans.factory.BeanScanner;
import com.diy.framework.web.mvc.view.JspViewResolver;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private final Map<String, Controller> controllers = new HashMap<>();
    private final ViewResolver viewResolver = new JspViewResolver();

    @Override
    public void init() {
        // BeanScanner로 @Copmonent 클래스 스캔
        final BeanScanner scanner = new BeanScanner("com.diy");
        final Set<Class<?>> beanClasses = scanner.scanClassesTypeAnnotatedWith(Component.class);

        // BeanFactory로 빈 생성 + 의존성 주입
        final BeanFactory beanFactory = new BeanFactory();
        beanFactory.createBeans(beanClasses);

        // Controller 타입 빈만 꺼내서 URL 매핑 (수동 등록)
        controllers.put("/lectures", beanFactory.getBean(LectureController.class));
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        final String uri = req.getRequestURI();
        final Controller controller = controllers.get(uri);

        try {
            final ModelAndView mav = controller.handleRequest(req, resp);
            render(mav, req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
