package com.diy.framework.web.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HtmlView {
    private final String viewName;

    public HtmlView(final String viewName) {
        if (viewName == null || viewName.isBlank()) {
            throw new IllegalArgumentException("viewName is required");
        }
        this.viewName = "/" + viewName + ".html";
    }

    public void render(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final var viewFile = readViewFile(req);
        resp.setContentType("text/html;charset=utf-8");
        final var writer = resp.getWriter();
        writer.print(viewFile);
    }

    private String readViewFile(final HttpServletRequest request) {
        final var content = new StringBuilder();
        final var viewPath = getViewPath(request);

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(viewPath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content.toString();
    }

    private String getViewPath(final HttpServletRequest request) {
        final var servletContext = request.getServletContext();
        return servletContext.getRealPath(viewName);
    }
}
