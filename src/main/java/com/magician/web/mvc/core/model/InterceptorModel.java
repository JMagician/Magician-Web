package com.magician.web.mvc.core.model;

import com.magician.web.mvc.core.interceptor.MagicianInterceptor;

import java.lang.reflect.Method;

/**
 * Interceptor entity class
 */
public class InterceptorModel {

    /**
     * method before the route is executed
     */
    private Method beforeMethod;

    /**
     * method after the route is executed
     */
    private Method afterMethod;

    /**
     * interceptor object
     */
    private MagicianInterceptor magicianInterceptor;
    /**
     * interceptor class
     */
    private Class cls;

    public Method getBeforeMethod() {
        return beforeMethod;
    }

    public void setBeforeMethod(Method beforeMethod) {
        this.beforeMethod = beforeMethod;
    }

    public Method getAfterMethod() {
        return afterMethod;
    }

    public void setAfterMethod(Method afterMethod) {
        this.afterMethod = afterMethod;
    }

    public MagicianInterceptor getMagicianInterceptor() {
        return magicianInterceptor;
    }

    public void setMagicianInterceptor(Object obj) {
        this.magicianInterceptor = (MagicianInterceptor) obj;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }
}
