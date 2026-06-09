package com.diy.framework.web.interceptor;

import com.diy.framework.context.annotation.Component;
import com.diy.framework.web.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class ResponseTimeMeasurementHandlerInterceptor implements HandlerInterceptor {

    private long preTime;

    @Override
    public boolean preHandle(final HttpServletRequest req, final HttpServletResponse resp) {
        preTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();;
        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest req, final HttpServletResponse resp, ModelAndView mv) {

    }

    @Override
    public void afterCompletion(final HttpServletRequest req, final HttpServletResponse resp) {
        long postTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        System.out.println("measure time = " + (postTime - preTime));
    }
}
