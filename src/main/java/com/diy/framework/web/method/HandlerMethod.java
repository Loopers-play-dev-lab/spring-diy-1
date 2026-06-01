package com.diy.framework.web.method;

import com.diy.framework.context.annotation.RestController;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class HandlerMethod {

    private final Object bean;
    private final Method method;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public HandlerMethod(final Object bean, final Method method) {
        this.bean = bean;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        try {
            method.setAccessible(true);

            final Object[] parameters = Arrays.stream(method.getParameterTypes())
                    .map(parameterType -> {
                        if (ServletRequest.class.isAssignableFrom(parameterType)) return req;
                        else if (ServletResponse.class.isAssignableFrom(parameterType)) return res;

                        throw new RuntimeException("Not supported Parameter Type: " + parameterType);
                    }).toArray();

            final Object view = this.method.invoke(bean, parameters);

            if (bean.getClass().isAnnotationPresent(RestController.class)) {
                renderJson(res, view);

                return null;
            }

            final Map<String, Object> model = new HashMap<>();

            req.getAttributeNames().asIterator().forEachRemaining(name -> {
                Object value = req.getAttribute(name);
                model.put(name, value);
            });

            return new ModelAndView(view.toString(), model);
        } catch (final Exception e) {
            throw e;
        } finally {
            method.setAccessible(false);
        }
    }

    private void renderJson(HttpServletResponse res, Object view) throws IOException {
        res.setContentType("application/json; charset=UTF-8");

        try (var outputStream = res.getOutputStream()) {
            objectMapper.writeValue(outputStream, view);
            outputStream.flush();
        }
    }
}
