package com.diy.framework.web.view;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface View {

    void render(final HttpServletRequest req, final HttpServletResponse res) throws IOException, ServletException;
}
