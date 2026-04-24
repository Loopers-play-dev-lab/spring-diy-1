package com.diy.framework;

import java.util.Map;

@FunctionalInterface
public interface Controller {

    ModelAndView handleRequest(Map<String, ?> params) throws Exception;
}
