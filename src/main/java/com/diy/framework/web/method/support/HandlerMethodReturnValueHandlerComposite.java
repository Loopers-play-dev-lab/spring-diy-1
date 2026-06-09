package com.diy.framework.web.method.support;

import com.diy.framework.web.method.HandlerMethod;
import com.diy.framework.web.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandlerMethodReturnValueHandlerComposite implements HandlerMethodReturnValueHandler {

    private final List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();

    public HandlerMethodReturnValueHandlerComposite addHandlers(final List<? extends HandlerMethodReturnValueHandler> handlers) {
        if (handlers != null) returnValueHandlers.addAll(handlers);
        return this;
    }

    @Override
    public boolean supportsReturnType(final HandlerMethod handlerMethod) {
        return selectHandler(handlerMethod) != null;
    }

    @Override
    public ModelAndView handleReturnValue(final Object returnValue,
                                          final HandlerMethod handlerMethod,
                                          final HttpServletRequest request,
                                          final HttpServletResponse response) throws Exception {
        final HandlerMethodReturnValueHandler handler = selectHandler(handlerMethod);
        if (handler == null) {
            throw new IllegalStateException(handlerMethod + " 에 적절한 HandlerMethodReturnValueHandler 없음.");
        }
        return handler.handleReturnValue(returnValue, handlerMethod, request, response);
    }

    private HandlerMethodReturnValueHandler selectHandler(final HandlerMethod handlerMethod) {
        for (final HandlerMethodReturnValueHandler handler : returnValueHandlers) {
            if (handler.supportsReturnType(handlerMethod)) return handler;
        }
        return null;
    }
}
