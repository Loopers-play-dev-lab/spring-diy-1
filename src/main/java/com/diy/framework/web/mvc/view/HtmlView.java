package com.diy.framework.web.mvc.view;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlView implements View {

    final Pattern pattern = Pattern.compile("<!--\\s*\"\\$\\{([^}]+)\\}\"\\s*-->");

    private final String viewName;

    public HtmlView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        final String viewFile = readViewFile(req, model);

        res.setContentType("text/html;charset=utf-8");
        final PrintWriter writer = res.getWriter();
        writer.print(viewFile);
    }

    private String readViewFile(final HttpServletRequest req, final Map<String, ?> model) {
        final StringBuilder content = new StringBuilder();

        final String viewPath = getViewPath(req);
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(viewPath), StandardCharsets.UTF_8))) {
            String line;


            while ((line = reader.readLine()) != null) {
                final Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    final String modelKey = matcher.group(1);
                    final List<Map<String, ?>> lectures = (List<Map<String, ?>>) model.get(modelKey);

                    lectures.forEach(lecture -> {
                        lecture.forEach((key, value) ->
                                content.append("<li>")
                                        .append(key)
                                        .append(": ")
                                        .append(value)
                                        .append("</li>")
                                        .append("\n"));

                        content.append("<br>").append("\n");
                    });

                    continue;
                }

                content.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return content.toString();
    }

    private String getViewPath(final HttpServletRequest req) {
        final ServletContext sc = req.getServletContext();
        return sc.getRealPath(viewName);
    }
}