package com.diy.app;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

public record HtmlView(
        String viewName
) implements View {
    @Override
    public void render(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        final String viewFile = readViewFile(req);

        res.setContentType("text/html;charset=utf-8");
        final PrintWriter writer = res.getWriter();
        writer.print(viewFile);
    }

    private String readViewFile(final HttpServletRequest request) {
        final StringBuilder content = new StringBuilder();
        final String viewPath = getViewPath().apply(request);

        try(final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(viewPath), StandardCharsets.UTF_8))) {
            String line;

            while((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return content.toString();
    }

    private Function<HttpServletRequest, String> getViewPath() {
        return request -> request.getServletContext().getRealPath(viewName);
    }
}
