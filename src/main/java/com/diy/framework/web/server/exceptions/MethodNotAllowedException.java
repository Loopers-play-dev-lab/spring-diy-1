package com.diy.framework.web.server.exceptions;

import javax.servlet.http.HttpServletResponse;

public class MethodNotAllowedException extends CustomException {

  public MethodNotAllowedException() {
    super(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다.");
  }
}
