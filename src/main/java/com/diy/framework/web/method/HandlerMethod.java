package com.diy.framework.web.method;

import com.diy.framework.context.annotation.RestController;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

            final byte[] bodyBytes = req.getInputStream().readAllBytes();
            final String body = new String(bodyBytes, StandardCharsets.UTF_8);

            AtomicInteger bodyParamCount = new AtomicInteger();

            final Object[] parameters = Arrays.stream(method.getParameterTypes())
                    .map(parameterType -> {
                        if (ServletRequest.class.isAssignableFrom(parameterType)) return req;
                        else if (ServletResponse.class.isAssignableFrom(parameterType)) return res;
                        else
                            bodyParamCount.getAndIncrement();
                            return deserializedBody(parameterType, body);
                    }).toArray();

            if(bodyParamCount.get() > 1) {
                throw new RuntimeException("controller에 사용 가능한 body parameter은 1개 입니다.");
            }

            final Object view = this.method.invoke(bean, parameters);

            final Map<String, Object> model = new HashMap<>();

            req.getAttributeNames().asIterator().forEachRemaining(name -> {
                Object value = req.getAttribute(name);
                model.put(name, value);
            });

            if (bean.getClass().isAnnotationPresent(RestController.class)) {
                renderJson(res, view);

                return null;
            }

            return new ModelAndView(view.toString(), model);
        } catch (final Exception e) {
            throw e;
        } finally {
            method.setAccessible(false);
        }
    }

    private Object deserializedBody(Class<?> type, String body) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructType(type);

            return objectMapper.readValue(body, javaType);

        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // renderJson의 책임이 HandlerMethod가 맞을까?
    private void renderJson(HttpServletResponse res, Object view) throws IOException {
        res.setContentType("application/json; charset=UTF-8");

        try (var outputStream = res.getOutputStream()) {
            objectMapper.writeValue(outputStream, view);
            outputStream.flush();
        }
    }
}
