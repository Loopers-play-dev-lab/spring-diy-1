package com.diy.framework.web.utils;

public interface ControllerV1 {
    ResponseV1 handle(RequestBodyV1 body) throws Exception;
}
