package com.diy.framework.web.servlet.handler;

import com.diy.framework.context.support.ApplicationObjectSupport;
import com.diy.framework.core.Ordered;
import com.diy.framework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractHandlerMapping extends ApplicationObjectSupport implements HandlerMapping, Ordered {
    private int order = 0;

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    protected abstract Object getHandlerInternal(HttpServletRequest req) throws Exception;

    public Object getHandler(HttpServletRequest req) throws Exception {
        return getHandlerInternal(req);
    }
}
