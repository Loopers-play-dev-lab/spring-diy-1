package com.diy.app.servlet;

import javax.servlet.Servlet;

public record ServletContext(
        String path,
        String servletName,
        Servlet servlet
) {
}
