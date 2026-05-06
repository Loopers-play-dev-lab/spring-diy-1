package com.diy.framework.web.mvc;

import com.diy.framework.web.mvc.view.JspViewResolver;
import com.diy.framework.web.mvc.view.View;
import com.diy.framework.web.mvc.view.ViewResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/")
public class FrontController extends HttpServlet {

    private static final Map<String, HandlerMethod> ROUTES = new HashMap<>();
    private final ViewResolver viewResolver = new JspViewResolver();

    // Spring은 패키지 스캔으로 자동화하지만, 여기서는 외부 등록 방식으로 단순화.
    public static void register(Object controller) {
        for (Method method : controller.getClass().getDeclaredMethods()) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            if (mapping == null) continue;

            String key = mapping.method() + " " + mapping.value();
            ROUTES.put(key, new HandlerMethod(controller, method));
            System.out.println("[등록] " + key + " → " + controller.getClass().getSimpleName() + "." + method.getName() + "()");
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        long start = System.currentTimeMillis();

        try {
            String key = req.getMethod() + " " + req.getRequestURI();
            HandlerMethod handler = ROUTES.get(key);
            if (handler == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            ModelAndView mav = handler.invoke(req, resp);
            View view = viewResolver.resolveViewName(mav.getViewName());
            view.render(mav.getModel(), req, resp);

        } catch (Exception e) {
            resp.sendError(500, e.getMessage()); // 예외처리 가능

        } finally {
            long elapsed = System.currentTimeMillis() - start;
            System.out.println("[" + req.getMethod() + " " + req.getRequestURI() + "] " + elapsed + "ms");  // 로깅 처리 가능
        }
    }

    private static class HandlerMethod {
        private final Object instance;
        private final Method method;

        HandlerMethod(Object instance, Method method) {
            this.instance = instance;
            this.method = method;
        }

        ModelAndView invoke(HttpServletRequest req, HttpServletResponse resp) throws Exception {
            try {
                return (ModelAndView) method.invoke(instance, req, resp);
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof Exception) throw (Exception) cause;
                throw e;
            }
        }
    }
}
