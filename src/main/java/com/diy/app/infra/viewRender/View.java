package com.diy.app.infra.viewRender;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface View {
    void render(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException;
}
