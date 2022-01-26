package com.magician.web.mvc.core.annotation;

import com.magician.web.mvc.core.constant.ReqMethod;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Route {
    /**
     * 路径
     * @return
     */
    String value();

    /**
     * 请求方式
     * @return
     */
    ReqMethod[] requestMethod() default ReqMethod.GET;
}
