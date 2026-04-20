package com.diy.framework.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

import static com.diy.framework.web.Lecture.*;

@WebServlet("/lectures")
public class LectureServlet extends HttpServlet {

    private LectureRepository lectureRepository;
    private ObjectMapper objectMapper;

    // init 은 당연히 1번만 불러옴
    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("config = " + config);
        lectureRepository = new LectureRepository();
        objectMapper = new ObjectMapper();
        super.init(config);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("req = " + req);
        System.out.println("resp = " + resp);
//        resp.getWriter().write("lectures");

        // service 내부 구현 -> doGet, doPost, doDelete, do~~ 걸려 있음. 삭제하면 동작 x
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final RequestDispatcher requestDispatcher = req.getRequestDispatcher("lecture-list.jsp");
        final Collection<Lecture> lectures = lectureRepository.findAll();

        System.out.println("lectures = " + lectures);

        req.setAttribute("lectures", lectures);
        requestDispatcher.forward(req, resp);
        // 반면 do~~ 내부 구현 -> 400 (default) 혹은 405 (1.1)
//        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String json = req.getReader().readLine();

        JsonNode rootNode = objectMapper.readTree(json);
        String name = rootNode.path("name").toString();
        long priceValue = rootNode.path("price").asLong();
        Price price = Price.of(priceValue);

        lectureRepository.save(Lecture.open(
                name, price
        ));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String json = req.getReader().readLine();

        JsonNode rootNode = objectMapper.readTree(json);
        long id = rootNode.path("id").asLong();
        String inputName = rootNode.path("name").toString();
        long inputPriceValue = rootNode.path("price").asLong();
        Price inputPrice = Price.of(inputPriceValue);

        Lecture lecture = lectureRepository.findById(id);
        lectureRepository.save(lecture.edit(inputName, inputPrice));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String json = req.getReader().readLine();

        JsonNode rootNode = objectMapper.readTree(json);
        long id = rootNode.path("id").asLong();

        Lecture lecture = lectureRepository.findById(id);
        lectureRepository.delete(lecture);
    }
}
