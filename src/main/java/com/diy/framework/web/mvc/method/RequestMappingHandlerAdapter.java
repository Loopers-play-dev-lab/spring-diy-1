package com.diy.framework.web.mvc.method.annotation;

import com.diy.framework.web.method.HandlerMethod;
import com.diy.framework.web.method.support.HandlerMethodArgumentResolver;
import com.diy.framework.web.method.support.HandlerMethodArgumentResolverComposite;
import com.diy.framework.web.method.support.HandlerMethodReturnValueHandler;
import com.diy.framework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.servlet.HandlerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class RequestMappingHandlerAdapter implements HandlerAdapter {

    private final HandlerMethodArgumentResolverComposite argumentResolvers;
    private final HandlerMethodReturnValueHandlerComposite returnValueHandlers;

    public RequestMappingHandlerAdapter(final List<HandlerMethodArgumentResolver> argumentResolvers,
                                        final List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        this.argumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(argumentResolvers);
        this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite().addHandlers(returnValueHandlers);
    }

    @Override
    public boolean supports(final Object handler) {
        return (handler instanceof HandlerMethod);
    }

    @Override
    public ModelAndView handle(final HttpServletRequest req, final HttpServletResponse res, final Object handler) throws Exception {
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        final Object returnValue = handlerMethod.invokeForRequest(req, res, argumentResolvers);
        return returnValueHandlers.handleReturnValue(returnValue, handlerMethod, req, res);
    }
}
