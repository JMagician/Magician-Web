package com.magician.web.mvc.core.annotation;

import com.magician.web.mvc.core.constant.ReqMethod;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Route {
    /**
     * path
     * @return
     */
    String value();

    /**
     * request method
     * @return
     */
    ReqMethod[] requestMethod() default ReqMethod.GET;
}
