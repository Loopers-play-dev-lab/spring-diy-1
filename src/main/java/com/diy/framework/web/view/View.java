package com.diy.framework.web.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface View {
    void render(Model model, HttpServletRequest req, HttpServletResponse res) throws Exception;
}
