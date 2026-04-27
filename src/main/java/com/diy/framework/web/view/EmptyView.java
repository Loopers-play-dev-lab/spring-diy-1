package com.diy.framework.web.view;

import com.diy.framework.web.utils.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EmptyView implements View {
    @Override
    public boolean isRender(HttpServletRequest req, String viewName) {
        return false;
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception {
        res.setContentType("text/html;charset=utf-8");
        res.setCharacterEncoding("utf-8");
        res.setStatus(HttpServletResponse.SC_OK);
    }
}
