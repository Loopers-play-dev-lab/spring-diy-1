package com.diy.app.view;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class HtmlView implements View {
    private final String viewPath;

    public HtmlView(String viewName) {
        this.viewPath = viewName;
    }

    @Override
    public void render(HttpServletRequest req,HttpServletResponse res) throws IOException {
        final String viewFile = readViewFile(req);

        res.setContentType("text/html;charset=utf-8");
        final PrintWriter writer = res.getWriter();
        writer.print(viewFile);
    }

    private String readViewFile(HttpServletRequest request) {
        StringBuilder content = new StringBuilder();
        String viewPath = getViewPath(request);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(viewPath), StandardCharsets.UTF_8))) {
            String line;

            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return content.toString();
    }

    private String getViewPath(HttpServletRequest req) {
        ServletContext sc = req.getServletContext();
        return sc.getRealPath(viewPath);
    }
}
