package com.diy.framework.web.server.exceptions;

import javax.servlet.http.HttpServletResponse;

public class NotFoundException extends CustomException {

  public NotFoundException() {
    super(HttpServletResponse.SC_NOT_FOUND, "존재하지 않는 요청입니다.");
  }
}
