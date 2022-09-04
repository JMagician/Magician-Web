package com.magician.web.mvc.load;

import com.magician.web.mvc.core.annotation.Interceptor;
import com.magician.web.mvc.core.annotation.Route;
import com.magician.web.mvc.core.cache.MagicianWebCacheManager;
import com.magician.web.mvc.core.constant.MagicianWebConstant;
import com.magician.web.mvc.core.model.InterceptorModel;
import com.magician.web.mvc.core.model.RouteModel;
import com.magician.web.commons.util.MatchUtil;
import io.magician.application.request.MagicianRequest;
import io.magician.common.cache.MagicianCacheManager;

import java.lang.reflect.Method;
import java.util.*;

/**
 * load route
 */
public class MvcLoad {

    /**
     * Whether it has been scanned, false no
     */
    private static boolean scan = false;

    /**
     * load route
     *
     * @throws Exception
     */
    public static void load() throws Exception {
        if (scan) {
            return;
        }

        Set<String> scanClassList = MagicianCacheManager.getScanClassList();

        /* Filter and create interceptors */
        Map<String, InterceptorModel> interceptorModelMap = scanInterceptor(scanClassList);

        /* Filter and create routes */
        scanRoute(scanClassList);

        /* After comparing the interceptor with the route, store it by category */
        Map<String, RouteModel> routeModelMap = MagicianWebCacheManager.getRouteMap();
        if (routeModelMap != null && routeModelMap.size() > 0) {
            for (String routePath : routeModelMap.keySet()) {
                for (String interPattern : interceptorModelMap.keySet()) {
                    if (MatchUtil.isMatch(interPattern, routePath)) {
                        MagicianWebCacheManager.setInterceptorMap(routePath, interceptorModelMap.get(interPattern));
                    }
                }
            }
        }

        scan = true;
    }

    /**
     * Scan interceptor
     *
     * @param scanClassList
     */
    private static Map<String, InterceptorModel> scanInterceptor(Set<String> scanClassList) throws Exception {
        Map<String, InterceptorModel> interceptorModelMap = new HashMap<>();
        for (String className : scanClassList) {
            Class<?> cls = Class.forName(className);
            Interceptor interceptor = cls.getAnnotation(Interceptor.class);
            if (interceptor == null) {
                continue;
            }

            InterceptorModel interceptorModel = new InterceptorModel();
            interceptorModel.setCls(cls);
            interceptorModel.setMagicianInterceptor(cls.getDeclaredConstructor().newInstance());
            interceptorModel.setBeforeMethod(cls.getMethod(MagicianWebConstant.BEFORE, new Class[]{MagicianRequest.class}));
            interceptorModel.setAfterMethod(cls.getMethod(MagicianWebConstant.AFTER, new Class[]{MagicianRequest.class, Object.class}));

            interceptorModelMap.put(interceptor.pattern().toUpperCase(), interceptorModel);
        }

        return interceptorModelMap;
    }

    /**
     * scan route
     *
     * @param scanClassList
     * @throws Exception
     */
    private static void scanRoute(Set<String> scanClassList) throws Exception {
        /* 从这些类中获取接口 */
        Map<String, RouteModel> routeModelMap = new HashMap<>();
        for (String className : scanClassList) {
            Class<?> cls = Class.forName(className);
            Method[] methods = cls.getMethods();
            for (Method method : methods) {
                if (method.getDeclaringClass().equals(cls)) {
                    Route methodRoute = method.getAnnotation(Route.class);
                    if (methodRoute == null) {
                        continue;
                    }
                    Route clsRoute = cls.getAnnotation(Route.class);
                    if (isVoid(method)) {
                        throw new Exception("[" + method + "]method has no return type");
                    }

                    String path = getRoute(methodRoute, clsRoute);
                    if (routeModelMap.containsKey(path)) {
                        throw new Exception("[" + path + "]Route is duplicated");
                    }

                    RouteModel routeModel = new RouteModel();
                    routeModel.setCls(cls);
                    routeModel.setMethod(method);
                    routeModel.setReqMethods(methodRoute.requestMethod());
                    routeModel.setRoute(path);
                    routeModel.setObject(cls.getDeclaredConstructor().newInstance());

                    routeModelMap.put(routeModel.getRoute(), routeModel);
                }
            }
        }
        MagicianWebCacheManager.saveRouteMap(routeModelMap);
    }

    /**
     * Get the request path of the route
     *
     * @param methodRoute
     * @param clsRoute
     * @return
     */
    private static String getRoute(Route methodRoute, Route clsRoute) {
        String methodPath = methodRoute.value();
        if (methodPath.endsWith("/")) {
            methodPath = methodPath.substring(0, methodPath.length() - 1);
        }
        if (methodPath.startsWith("/")) {
            methodPath = methodPath.substring(1);
        }

        if (clsRoute != null) {
            String clsPath = clsRoute.value();
            if (clsPath.endsWith("/")) {
                clsPath = clsPath.substring(0, clsPath.length() - 1);
            }
            if (clsPath.startsWith("/")) {
                clsPath = clsPath.substring(1);
            }
            methodPath = clsPath + "/" + methodPath;
        }

        if (!methodPath.startsWith("/")) {
            methodPath = "/" + methodPath;
        }

        return methodPath.toUpperCase();
    }

    /**
     * Whether the return type is void
     *
     * @param method
     * @return
     */
    private static boolean isVoid(Method method) {
        Class cl = method.getReturnType();
        String st = cl.getName();
        return st.toLowerCase().trim().equals("void");
    }
}
