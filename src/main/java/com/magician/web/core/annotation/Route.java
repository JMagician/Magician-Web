package com.magician.web.core.annotation;


import io.magician.tcp.codec.impl.http.constant.ReqMethod;

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
