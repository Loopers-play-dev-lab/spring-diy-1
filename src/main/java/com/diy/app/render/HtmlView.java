package com.diy.app.render;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HtmlView implements View {

    private final String viewName;

    public HtmlView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, Object> model, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        final String viewFile = readViewFile(viewName, req);

        res.setContentType("text/html;charset=utf-8");
        final PrintWriter writer = res.getWriter();
        writer.print(viewFile);
    }

    private String readViewFile(final String viewName, final HttpServletRequest request) {
        final StringBuilder content = new StringBuilder();
        final String viewPath = getViewPath(viewName, request);

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

    private String getViewPath(final String viewName, final HttpServletRequest request) {
        final ServletContext sc = request.getServletContext();
        return sc.getRealPath(viewName);
    }
}
