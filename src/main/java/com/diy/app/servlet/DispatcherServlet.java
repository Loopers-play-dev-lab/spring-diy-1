package com.diy.app.servlet;

import com.diy.app.controller.Controller;
import com.diy.app.controller.LectureController;
import com.diy.app.render.JspView;
import com.diy.app.render.View;
import com.diy.app.resolver.ViewResolver;
import com.diy.framework.web.annotation.Autowired;
import com.diy.framework.web.annotation.Component;
import com.diy.framework.web.bean.factory.BeanFactory;
import com.diy.framework.web.bean.factory.BeanScanner;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    final Map<String, Controller> controllers = new HashMap<>();
    private final ViewResolver viewResolver = new ViewResolver();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            initBeans();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        LectureController lectureController = (LectureController) BeanFactory
                .getBeanFactory()
                .getBean("LectureController");

        controllers.put("/lectures", lectureController);
    }

    private static void initBeans() throws Exception {
        BeanScanner scanner = new BeanScanner("com.diy.app");
        Set<Class<?>> beans = scanner.scanClassesTypeAnnotatedWith(Component.class);

        Map<String, Object> createdBeans = new HashMap<>();

        // bean 생성 (기본 생성자)
        for (Class<?> bean : beans) {
            final Constructor<?> constructor = bean.getDeclaredConstructors()[0];
            Object createdBean = null;

            createdBean = constructor.newInstance();
            createdBeans.put(bean.getSimpleName(), createdBean);
        }

        // Autowired 적용
        for (Object bean : createdBeans.values()) {
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                Annotation annotation = field.getAnnotation(Autowired.class);
                if (annotation != null) {
                    field.setAccessible(true);
                    field.set(bean, createdBeans.get(field.getType().getSimpleName()));
                    field.setAccessible(false);
                }
            }
        }

        BeanFactory beanFactory = BeanFactory.getBeanFactory();
        createdBeans.values().forEach(beanFactory::addBean);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        Controller controller = controllers.get(requestURI);

        try {
            ModelAndView modelAndView = controller.handleRequest(req, resp);
            if (modelAndView != null) {
                render(modelAndView, req, resp);
            }
        } catch (Exception ignored) {

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