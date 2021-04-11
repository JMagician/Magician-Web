package com.magician.web.core.model;

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
    private Object object;
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

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }
}
