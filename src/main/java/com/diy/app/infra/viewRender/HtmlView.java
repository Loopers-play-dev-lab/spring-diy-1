package com.diy.app.infra.viewRender;

import com.diy.app.business.domain.Lecture;
import com.diy.app.infra.dto.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class HtmlView implements View {

    private static View instance = null;

    public static View getInstance() {
        if (Objects.isNull(instance)) instance = new HtmlView();
        return instance;
    }

    public void render(final HttpServletRequest req, final HttpServletResponse res, final ModelAndView modelAndView) throws IOException {
        final String filePath = getClass().getClassLoader()
                .getResource(modelAndView.getViewName() + ".html")
                .getFile().split("resources/")[1];
        String viewFile = readViewFile(req, filePath);
        viewFile = resolveTemplate(viewFile, req);

        res.setContentType("text/html;charset=utf-8");
        final PrintWriter writer = res.getWriter();
        writer.print(viewFile);
    }

    @Override
    public boolean match(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) {
        return Objects.nonNull(getClass().getClassLoader().getResource(modelAndView.getViewName() + ".html"));
    }

    private String resolveTemplate(String html, HttpServletRequest req) {
        final List<Lecture> lectures = (List<Lecture>) req.getAttribute("lectures");
        final StringBuilder sb = new StringBuilder();
        if (lectures != null) {
            for (Lecture lecture : lectures) {
                sb.append("<li>id: ").append(lecture.getId()).append("</li>\n");
                sb.append("<li>name: ").append(lecture.getName()).append("</li>\n");
                sb.append("<li>price: ").append(lecture.getPrice()).append("</li>\n");
                sb.append("<br>\n");
            }
        }
        html = html.replace("\"${lectures}\"", sb.toString());

        return html;
    }

    private String readViewFile(final HttpServletRequest request, final String filePath) {
        final StringBuilder content = new StringBuilder();
        final String viewPath = getViewPath(request, filePath);

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

    private String getViewPath(final HttpServletRequest request, final String filePath) {
        final ServletContext sc = request.getServletContext();
        return sc.getRealPath(filePath);
    }

}
