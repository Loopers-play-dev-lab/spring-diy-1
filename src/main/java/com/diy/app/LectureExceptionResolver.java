package com.diy.app;

import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.servlet.HandlerExceptionResolver;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

// @ControllerAdvice 도입 전 방식. 빈 등록은 안 하고 비교용으로만 남겨둠.
public class LectureExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(final HttpServletRequest request,
                                         final HttpServletResponse response,
                                         final Object handler,
                                         final Exception ex) {
        if (!(ex instanceof IllegalArgumentException)) {
            return null;
        }

        try {
            response.setStatus(400);
            response.setContentType("application/json;charset=UTF-8");

            final Map<String, Object> body = new LinkedHashMap<>();
            body.put("error", "BAD_REQUEST");
            body.put("message", ex.getMessage());

            objectMapper.writeValue(response.getOutputStream(), body);
            return new ModelAndView(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
