package com.diy.framework.web.mvc.view;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface View {
    public void render(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException, ServletException, IOException;
}
