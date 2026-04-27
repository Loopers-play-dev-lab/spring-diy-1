package com.diy.framework.web.view;

import com.diy.framework.web.utils.ResponseV1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EmptyView implements ViewV1 {
    @Override
    public boolean isRender(HttpServletRequest req, String viewName) {
        return false;
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse res, ResponseV1 responseV1) throws Exception {
        res.setContentType("text/html;charset=utf-8");
        res.setCharacterEncoding("utf-8");
        res.setStatus(HttpServletResponse.SC_OK);
    }
}
