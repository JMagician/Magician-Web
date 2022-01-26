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
 * 加载接口
 */
public class ApiLoad {

    /**
     * 是否扫描过了，false否
     */
    private static boolean scan = false;

    /**
     * 加载接口
     * @throws Exception
     */
    public static void load() throws Exception {
        if(scan){
            return;
        }

        Set<String> scanClassList = MagicianCacheManager.getScanClassList();

        /* 筛选并创建拦截器 */
        Map<String, InterceptorModel> interceptorModelMap = scanInterceptor(scanClassList);

        /* 筛选并创建接口 */
        scanRoute(scanClassList);

        /* 将拦截器跟route比对后，分类存放 */
        Map<String, RouteModel> routeModelMap = MagicianWebCacheManager.getRouteMap();
        if(routeModelMap != null && routeModelMap.size() > 0){
            for(String routePath : routeModelMap.keySet()){
                for(String interPattern : interceptorModelMap.keySet()) {
                    if (MatchUtil.isMatch(interPattern, routePath)) {
                        MagicianWebCacheManager.setInterceptorMap(routePath, interceptorModelMap.get(interPattern));
                    }
                }
            }
        }

        scan = true;
    }

    /**
     * 扫描拦截器
     * @param scanClassList
     */
    private static Map<String, InterceptorModel> scanInterceptor(Set<String> scanClassList) throws Exception {
        Map<String, InterceptorModel> interceptorModelMap = new HashMap<>();
        for(String className : scanClassList){
            Class<?> cls = Class.forName(className);
            Interceptor interceptor = cls.getAnnotation(Interceptor.class);
            if(interceptor == null){
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
     * 扫描接口
     * @param scanClassList
     * @throws Exception
     */
    private static void scanRoute(Set<String> scanClassList) throws Exception {
        /* 从这些类中获取接口 */
        Map<String, RouteModel> routeModelMap = new HashMap<>();
        for(String className : scanClassList){
            Class<?> cls = Class.forName(className);
            Method[] methods = cls.getMethods();
            for(Method method : methods){
                if(method.getDeclaringClass().equals(cls)){
                    Route methodRoute = method.getAnnotation(Route.class);
                    if(methodRoute == null){
                        continue;
                    }
                    Route clsRoute = cls.getAnnotation(Route.class);
                    if(isVoid(method)){
                        throw new Exception("["+method+"]方法没有返回类型");
                    }

                    String path = getRoute(methodRoute, clsRoute);
                    if(routeModelMap.containsKey(path)){
                        throw new Exception("["+path+"]Route有重复");
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
     * 获取接口的请求路径
     * @param methodRoute
     * @param clsRoute
     * @return
     */
    private static String getRoute(Route methodRoute, Route clsRoute){
        String methodPath = methodRoute.value();
        if(methodPath.endsWith("/")){
            methodPath = methodPath.substring(0, methodPath.length() - 1);
        }
        if(methodPath.startsWith("/")){
            methodPath = methodPath.substring(1);
        }

        if(clsRoute != null){
            String clsPath = clsRoute.value();
            if(clsPath.endsWith("/")){
                clsPath = clsPath.substring(0, clsPath.length() - 1);
            }
            if(clsPath.startsWith("/")){
                clsPath = clsPath.substring(1);
            }
            methodPath = clsPath + "/" + methodPath;
        }

        if(!methodPath.startsWith("/")){
            methodPath = "/" + methodPath;
        }

        return methodPath.toUpperCase();
    }

    /**
     * 返回值是否是void
     * @param method
     * @return
     */
    private static boolean isVoid(Method method){
        Class cl = method.getReturnType();
        String st = cl.getName();
        return st.toLowerCase().trim().equals("void");
    }
}
