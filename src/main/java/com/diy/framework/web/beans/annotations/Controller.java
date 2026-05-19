package com.diy.framework.web.beans.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface Controller {
    String url() default "";
}
