package com.diy.framework.web.view;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HtmlView implements View{
    private final String viewName;

    public HtmlView(final String viewName) {
        this.viewName = viewName;
    }

    private String readViewFile(final HttpServletRequest request) {
        final StringBuilder content = new StringBuilder();
        final String viewPath = getViewPath(request);

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
        final ServletContext sc = request.getServletContext();
        return sc.getRealPath(viewName);
    }

    @Override
    public void render(Map<String, Object> model, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String viewFile = readViewFile(req);

        resp.setContentType("text/html;charset=utf-8");
        final PrintWriter writer = resp.getWriter();
        writer.print(viewFile);
    }

}
