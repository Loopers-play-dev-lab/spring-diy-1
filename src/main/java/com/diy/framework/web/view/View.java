package com.diy.framework.web.view;

import com.diy.framework.web.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface View {
    public boolean isRender(HttpServletRequest req, String viewName);
    public void render(final HttpServletRequest req, final HttpServletResponse res, Model model) throws Exception;
}
