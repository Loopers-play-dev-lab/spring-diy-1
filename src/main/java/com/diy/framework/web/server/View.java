package com.diy.framework.web.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface View {
    void render(final HttpServletRequest req, final HttpServletResponse resp) throws Exception;
}
