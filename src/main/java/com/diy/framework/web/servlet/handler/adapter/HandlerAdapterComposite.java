package com.diy.framework.web.servlet.handler.adapter;

import com.diy.framework.web.mvc.view.ModelAndView;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerAdapterComposite implements HandlerAdapter {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterComposite() {
        this.handlerAdapters = List.of(
                new ControllerHandlerAdapter(),
                new AnnotationHandlerAdapter()
        );
    }

    @Override
    public boolean supports(Object handler) {
        return handlerAdapters.stream()
                .anyMatch(adapter -> adapter.supports(handler));
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No adapter found"))
                .handle(handler, request, response);
    }
}
