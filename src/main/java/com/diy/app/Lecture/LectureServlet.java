package com.diy.app.Lecture;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {
	private final LectureRepository lectureRepository;

	public LectureServlet() {
		this.lectureRepository = new LectureRepository();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		List<Lecture> lectures = lectureRepository.findAll();
		req.setAttribute("lectures", lectures);
		req.getRequestDispatcher("/lecture-list.jsp").forward(req, res);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String name = req.getParameter("name");
		int price = Integer.parseInt(req.getParameter("price"));

		Lecture lecture = new Lecture(null, name, price);
		lectureRepository.save(lecture);

		res.sendRedirect("/lectures");
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Long id = Long.parseLong(req.getParameter("id"));
		String name = req.getParameter("name");
		int price = Integer.parseInt(req.getParameter("price"));

		Lecture lecture = new Lecture(id, name, price);
		lectureRepository.update(id, lecture);

		res.sendRedirect("/lectures");
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Long id = Long.parseLong(req.getParameter("id"));
		lectureRepository.deleteById(id);

		res.sendRedirect("/lectures");
	}
}
