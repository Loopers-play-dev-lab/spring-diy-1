package com.diy.framework.web.server.exceptions;

import java.lang.reflect.Constructor;
import javax.servlet.http.HttpServletResponse;

public class CannotCreateBeanException extends CustomException {

  public CannotCreateBeanException(Constructor<?> constructor, int order) {
    super(
        HttpServletResponse.SC_BAD_REQUEST,
        "Bean을 생성할 수 없습니다: " + constructor.getName() + " - constructor[" + order + "]"
    );
  }
}
