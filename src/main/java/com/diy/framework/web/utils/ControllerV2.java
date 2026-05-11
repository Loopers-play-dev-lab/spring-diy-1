package com.diy.framework.web.utils;

public interface ControllerV2 {
    ResponseV1 handle(String method, RequestBodyV1 body) throws Exception;
}
