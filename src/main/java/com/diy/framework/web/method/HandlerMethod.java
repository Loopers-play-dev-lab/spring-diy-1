package com.diy.framework.web.method;

import com.diy.framework.web.mvc.anotation.RestController;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class HandlerMethod {

    private final Object bean;
    private final Method method;

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

                        try {
                            final byte[] bodyBytes = req.getInputStream().readAllBytes();
                            final String body = new String(bodyBytes, StandardCharsets.UTF_8);
                            return new ObjectMapper().readValue(body, parameterType);
                        } catch (final IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).toArray();

            final Object view = this.method.invoke(bean, parameters);

            if (bean.getClass().isAnnotationPresent(RestController.class)) {
                final String json = new ObjectMapper().writeValueAsString(view);
                res.setContentType("application/json;charset=UTF-8");
                res.getWriter().write(json);
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
}
