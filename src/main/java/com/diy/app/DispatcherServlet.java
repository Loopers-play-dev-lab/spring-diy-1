package com.diy.app;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

//모든 요청을 받는 단일 서블릿
@WebServlet("/")
public class DispatcherServlet extends HttpServlet {

    private HandlerMapping handlerMapping;

    //서블릿 시작 시 HandlerMapping에 컨트롤러 등록
    @Override
    public void init(){
        handlerMapping = new HandlerMapping();
        handlerMapping.setMapping("GET", "/lectures", new LectureListController());
        handlerMapping.setMapping("POST", "/lectures", new LectureCreateController());
        handlerMapping.setMapping("PUT", "/lectures", new LectureUpdateController());
        handlerMapping.setMapping("DELETE", "/lectures", new LectureDeleteController());
    }

    // TODO: service() - 요청에서 컨트롤러 찾아서 handleRequest() 호출 컨트롤러 없으면 404
}
