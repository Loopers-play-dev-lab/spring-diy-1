package com.diy.framework.web.view;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HtmlView implements View {
    private final String viewName;

    public HtmlView(String viewName) {
        this.viewName = viewName;
    }

    private String readViewFile(final HttpServletRequest req){
        final StringBuilder content = new StringBuilder();
        final String viewPath = getViewPath(req);

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(viewPath), StandardCharsets.UTF_8))){
            String line;

            while((line = reader.readLine()) != null){
                content.append(line).append("\n");
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return content.toString();
    }

    private String getViewPath(final HttpServletRequest req){
        final ServletContext sc = req.getServletContext();
        return sc.getRealPath(viewName);
    }

	@Override
	public void render(Map<String, Object> model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		final String viewFile = readViewFile(req);
		resp.setContentType("text/html;charset=UTF-8");
		final PrintWriter writer = resp.getWriter();
		writer.print(viewFile);
	}
}
