package com.diy.framework.value;

public record ModelAndView(
        String viewName,
        Model model
) {
    public static ModelAndView fromViewName(String viewName) {
        return new ModelAndView(viewName, Model.empty());
    }

    public static ModelAndView of(String viewName, Model model) {
        return new ModelAndView(viewName, model);
    }
}
