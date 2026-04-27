package com.diy.framework.web.view;

import com.diy.framework.web.utils.ResponseV1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ViewV1 {
    public boolean isRender(HttpServletRequest req, String viewName);
    public void render(final HttpServletRequest req, final HttpServletResponse res, ResponseV1 model) throws Exception;
}
