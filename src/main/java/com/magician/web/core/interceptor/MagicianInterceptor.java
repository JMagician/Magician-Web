package com.magician.web.core.interceptor;


import io.magician.application.request.MagicianRequest;

/**
 * 拦截器基类
 */
public interface MagicianInterceptor {

    String SUCCESS = "success";

    /**
     * 接口执行之前
     * @param request
     * @return
     */
    Object before(MagicianRequest request);

    /**
     * 接口执行之后
     * @param request
     * @param result
     * @return
     */
    Object after(MagicianRequest request, Object result);
}
