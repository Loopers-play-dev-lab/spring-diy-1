package com.diy.framework.web.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectView implements View {

	private final String viewName;

	public RedirectView(String viewName) {
		this.viewName = viewName;
	}

	@Override
	public void render(Map<String, Object> model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		resp.sendRedirect(viewName);
	}
}
