package com.diy.framework.web.server.exceptions;

import javax.servlet.http.HttpServletResponse;

public class NotFoundViewException extends CustomException {

  public NotFoundViewException(String viewName) {
    super(HttpServletResponse.SC_NOT_FOUND, "View를 찾을 수 없습니다: " + viewName);
  }
}
