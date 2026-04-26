package com.diy.app;

import java.util.Collections;
import java.util.Map;

public record ModelAndView(
        String viewName,
        Map<String, Object> model
) {
    public static ModelAndView fromViewName(String viewName) {
        return new ModelAndView(viewName, Collections.emptyMap());
    }

    public static ModelAndView of(String viewName, Map<String, Object> model) {
        return new ModelAndView(viewName, model);
    }
}
