package com.diy.app.view;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HtmlView implements View {
    private final String viewPath;

    public HtmlView(String viewName) {
        this.viewPath = viewName;
    }

    @Override
    public void render(Map<String, Object> model, HttpServletRequest req, HttpServletResponse res) throws IOException {
        String viewContent = readViewFile(req);

        if (model != null) {
            for (String key : model.keySet()) {
                String value = String.valueOf(model.get(key));
                viewContent = viewContent.replace("${" + key + "}", value);
            }
        }

        res.setContentType("text/html;charset=utf-8");
        final PrintWriter writer = res.getWriter();
        writer.print(viewContent);
        writer.flush();
    }

    private String readViewFile(HttpServletRequest request) {
        StringBuilder content = new StringBuilder();
        String realPath = getViewPath(request);

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(realPath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("HTML 파일을 읽는 중 오류 발생: " + realPath, e);
        }

        return content.toString();
    }

    private String getViewPath(HttpServletRequest req) {
        ServletContext sc = req.getServletContext();
        return sc.getRealPath(viewPath);
    }
}