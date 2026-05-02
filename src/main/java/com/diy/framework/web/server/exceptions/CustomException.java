package com.diy.framework.web.server.exceptions;

public abstract class CustomException extends RuntimeException {

  private final int httpCode;

  protected CustomException(int httpCode, String message) {
    super(message);
    this.httpCode = httpCode;
  }

  public int getHttpCode() {
    return httpCode;
  }
}
