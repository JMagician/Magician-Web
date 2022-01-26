package com.magician.web.mvc.core.model;

import com.magician.web.mvc.core.interceptor.MagicianInterceptor;

import java.lang.reflect.Method;

/**
 * 拦截器实体类
 */
public class InterceptorModel {

    /**
     * 接口开始之前的方法
     */
    private Method beforeMethod;

    /**
     * 接口结束后的方法
     */
    private Method afterMethod;

    /**
     * 拦截器对象
     */
    private MagicianInterceptor magicianInterceptor;
    /**
     * 类型
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
