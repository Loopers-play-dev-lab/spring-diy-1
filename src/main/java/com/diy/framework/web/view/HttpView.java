package com.diy.framework.web.view;

import com.diy.framework.web.utils.Model;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class HttpView implements View {
    public HttpView() {
        super();
    }

    public boolean isRender(HttpServletRequest request, String viewName) {
        final ServletContext sc = request.getServletContext();
        String fileName = viewName + ".html";
        String realPath = sc.getRealPath(fileName);
        return realPath != null;
    }

    public void render(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception {
        final String viewFile = readViewFile(req, model.getViewName());

        res.setContentType("text/html;charset=utf-8");
        final PrintWriter writer = res.getWriter();
        writer.print(viewFile);
    }

    private String readViewFile(final HttpServletRequest request, String viewName) {
        final StringBuilder content = new StringBuilder();
        String fileName = viewName + ".html";
        final String viewPath = getViewPath(request, fileName);

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

    private String getViewPath(final HttpServletRequest request, String viewName) {
        final ServletContext sc = request.getServletContext();
        return sc.getRealPath(viewName);
    }
}
