package com.diy.framework.web.servlet.handler;

import com.diy.framework.context.support.ApplicationObjectSupport;
import com.diy.framework.core.Ordered;
import com.diy.framework.web.servlet.HandlerMapping;
import javax.servlet.http.HttpServletRequest;

public abstract class AbstractHandlerMapping extends ApplicationObjectSupport implements HandlerMapping, Ordered {

    private int order = LOWEST_PRECEDENCE;

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return getHandlerInternal(request);
    }

    protected abstract Object getHandlerInternal(HttpServletRequest request);
}
