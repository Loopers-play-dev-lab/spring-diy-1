package com.diy.app.controller;

import com.diy.app.application.LectureService;
import com.diy.app.domain.Lecture;
import com.diy.app.controller.dto.LectureCreateRequest;
import com.diy.app.controller.dto.LectureUpdateRequest;
import com.diy.framework.web.view.ModelAndView;
import com.diy.framework.web.controller.Controller;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LectureController implements Controller {

    private final LectureService lectureService = new LectureService();

    @Override
    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		if ("GET".equals(request.getMethod())) {
			return doGet(request, response);
		}

		if ("POST".equals(request.getMethod())) {
            return doPost(request, response);
        }

		if ("DELETE".equals(request.getMethod())) {
			return doDelete(request,response);
		}

		if ("PUT".equals(request.getMethod())) {
			return doPut(request,response);
		}

        throw new RuntimeException("404 Not Found");
    }

	private ModelAndView doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		final byte[] bodyBytes = req.getInputStream().readAllBytes();
		final String body = new String(bodyBytes, StandardCharsets.UTF_8);
		LectureCreateRequest request = new ObjectMapper().readValue(body, LectureCreateRequest.class);

		lectureService.register(request.name(), request.price());
        return new ModelAndView("redirect:/lectures");
    }

    private ModelAndView doGet(final HttpServletRequest req, final HttpServletResponse resp) throws Exception {
        final List<Lecture> lectures = lectureService.getLectures();

        final Map<String, Object> model = new HashMap<>();
        model.put("lectures", lectures);

        return new ModelAndView("lecture-list", model);
    }

	private ModelAndView doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		final byte[] bodyBytes = req.getInputStream().readAllBytes();
		final String body = new String(bodyBytes, StandardCharsets.UTF_8);
		LectureUpdateRequest request = new ObjectMapper().readValue(body, LectureUpdateRequest.class);

		lectureService.modify(request.id(), request.name(), request.price());
		return new ModelAndView("redirect:/lectures");
	}

	private ModelAndView doDelete(final HttpServletRequest req, final HttpServletResponse resp) {
		lectureService.delete(Long.parseLong(req.getParameter("id")));
		return new ModelAndView("redirect:/lectures");
	}
}
