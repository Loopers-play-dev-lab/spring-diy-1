package com.diy.framework.web.mvc.method;

import com.diy.framework.web.method.HandlerMethod;
import com.diy.framework.web.method.support.HandlerMethodReturnValueHandler;
import com.diy.framework.web.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class ViewNameMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    @Override
    public boolean supportsReturnType(final HandlerMethod handlerMethod) {
        final Class<?> returnType = handlerMethod.getMethod().getReturnType();
        return CharSequence.class.isAssignableFrom(returnType)
                || ModelAndView.class.isAssignableFrom(returnType);
    }

    @Override
    public ModelAndView handleReturnValue(final Object returnValue,
                                          final HandlerMethod handlerMethod,
                                          final HttpServletRequest request,
                                          final HttpServletResponse response) {
        if (returnValue instanceof ModelAndView mav) return mav;

        final Map<String, Object> model = new HashMap<>();
        request.getAttributeNames().asIterator().forEachRemaining(name -> model.put(name, request.getAttribute(name)));

        final String viewName = (returnValue == null) ? null : returnValue.toString();

        return new ModelAndView(viewName, model);
    }
}
