package com.diy.framework.web.server.mv;

import com.diy.app.Lecture;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class HtmlView implements View {
    private final String viewName;

    public HtmlView(final String viewName) {
        if (viewName == null || viewName.isBlank()) {
            throw new IllegalArgumentException("viewName is required");
        }
        this.viewName = viewName;
    }

    @Override
    public void render(final HttpServletRequest req, final HttpServletResponse resp, final ModelAndView mav) throws Exception {
        System.out.println("[HtmlView] render is called.");
        final var viewFile = readViewFile(req, mav.getModel());
        resp.setContentType("text/html;charset=utf-8");
        final var writer = resp.getWriter();
        writer.print(viewFile);
    }

    private String readViewFile(final HttpServletRequest request, final Model model) {
        final var content = new StringBuilder();
        final var viewPath = getViewPath(request);

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(viewPath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (isLecturesPlace(line)) {
                    if (model.containsAttribute("lectures")) {
                        final Collection<Lecture> lectures = (Collection<Lecture>) model.getAttribute("lectures");
                        for (Lecture lecture : lectures) {
                            content.append("<li>").append("id: ").append(lecture.getId()).append("</li>");
                            content.append("<li>").append("name: ").append(lecture.getName()).append("</li>");
                            content.append("<li>").append("price: ").append(lecture.getPrice()).append("</li>");
                            content.append("<br>").append("\n");
                        }
                    }
                    continue;
                }
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content.toString();
    }

    private static boolean isLecturesPlace(final String line) {
        return "<!--${lectures}-->".equalsIgnoreCase(line);
    }

    private String getViewPath(final HttpServletRequest request) {
        final var servletContext = request.getServletContext();
        return servletContext.getRealPath(viewName);
    }
}
