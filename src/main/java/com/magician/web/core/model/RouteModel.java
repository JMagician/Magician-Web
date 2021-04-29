package com.magician.web.core.model;

import io.magician.tcp.codec.impl.http.constant.ReqMethod;

import java.lang.reflect.Method;

public class RouteModel {

    private String route;

    private Method method;

    private Class cls;

    private Object object;

    private ReqMethod[] reqMethods;

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public ReqMethod[] getReqMethods() {
        return reqMethods;
    }

    public void setReqMethods(ReqMethod[] reqMethods) {
        this.reqMethods = reqMethods;
    }
}
