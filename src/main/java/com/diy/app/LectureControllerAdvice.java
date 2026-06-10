package com.diy.app;

import com.diy.framework.web.mvc.anotation.ControllerAdvice;
import com.diy.framework.web.mvc.anotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class LectureControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public Map<String, Object> handleIllegalArgument(final IllegalArgumentException ex,
                                                      final HttpServletResponse response) {
        response.setStatus(400);

        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "BAD_REQUEST");
        body.put("message", ex.getMessage());
        return body;
    }
}
