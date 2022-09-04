package com.magician.web.mvc.core.interceptor;


import io.magician.application.request.MagicianRequest;

/**
 * Interceptor base class
 */
public interface MagicianInterceptor {

    String SUCCESS = "success";

    /**
     * before routing
     * @param request
     * @return
     */
    Object before(MagicianRequest request);

    /**
     * after routing
     * @param request
     * @param result
     * @return
     */
    Object after(MagicianRequest request, Object result);
}
