package com.diy.framework.web.servlet;

import com.diy.framework.web.mvc.view.ModelAndView;
import com.diy.framework.web.servlet.handler.adapter.AnnotationHandlerAdapter;
import com.diy.framework.web.servlet.handler.adapter.ControllerHandlerAdapter;
import com.diy.framework.web.servlet.handler.adapter.HandlerAdapter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerAdapterComposite {

    private final List<HandlerAdapter> handlerAdapters = List.of(
            new ControllerHandlerAdapter(),
            new AnnotationHandlerAdapter()
    );

    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No adapter found"))
                .handle(handler, request, response);
    }
}
