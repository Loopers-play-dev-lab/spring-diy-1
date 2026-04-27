package com.diy.app;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

//URL + Method --> Controller 를 저장하고 찾아주는 클래스
public class HandlerMapping {

    //Map<String, Controller> 선언
    private Map<String, Controller> mapping = new HashMap<>();

    //매핑 등록 메서드
    public void setMapping (String method, String url, Controller controller){
        mapping.put(method + ":" + url, controller);
    }

    //요청에서 키를 만들어서 Map에서 컨트롤러 찾아 반환
    public Controller getController (HttpServletRequest req){
        return mapping.get(req.getMethod() + ":" + req.getRequestURI());
    }
}
