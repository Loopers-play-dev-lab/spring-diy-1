package com.diy.framework.web.view;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface View {
    void render(Map<String, Object> model, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException;
}
