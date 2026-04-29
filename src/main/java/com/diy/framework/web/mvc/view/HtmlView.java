package com.diy.framework.web.mvc.view;

import com.diy.app.domain.Lecture;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class HtmlView implements View {
    private final String viewName;

    public HtmlView(final String viewName) {
        this.viewName = viewName;
    }

    public void render(Map<String, Object> model, final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        String viewFile = readViewFile(req);
        viewFile = viewFile.replace("</body>", writeViewFile(req) + "</body>");

        res.setContentType("text/html;charset=utf-8");
        final PrintWriter writer = res.getWriter();
        writer.print(viewFile);
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

    private String writeViewFile(final HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();

        List<Lecture> list = (List<Lecture>) request.getAttribute("list");
        if (list != null) {
            sb.append("<h1>Lecture List</h1>");
            for (Lecture lecture : list) {
                sb.append("<div id=").append(lecture.getLectureId()).append(">");
                sb.append("<li>id: <a href=/lecture?lectureId=")
                        .append(lecture.getLectureId())
                        .append(">")
                        .append(lecture.getLectureId())
                        .append("</a></li>");
                sb.append("<li>name: ").append(lecture.getName()).append("</li>");
                sb.append("<li>price: ").append(lecture.getPrice()).append("</li>");
                sb.append("<button id=del-").append(lecture.getLectureId()).append(">delete</button>");
                sb.append("</div>");
                sb.append("<br>");
            }
        }
        return sb.toString();
    }
}