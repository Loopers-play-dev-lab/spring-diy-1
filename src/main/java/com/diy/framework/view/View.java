package com.diy.framework.view;

import com.diy.framework.value.Model;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface View {
    void render(final Model model, final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException;
}
