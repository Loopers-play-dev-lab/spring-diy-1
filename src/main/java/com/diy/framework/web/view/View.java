package com.diy.framework.web.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface View {
    void render(final HttpServletRequest req, final HttpServletResponse res) throws Exception;
}
