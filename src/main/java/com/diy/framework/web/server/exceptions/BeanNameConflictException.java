package com.diy.framework.web.server.exceptions;

import javax.servlet.http.HttpServletResponse;

public class BeanNameConflictException extends CustomException {

  public BeanNameConflictException(String key) {
    super(HttpServletResponse.SC_BAD_REQUEST, "동일한 이름의 Bean이 존재합니다: " + key);
  }
}
