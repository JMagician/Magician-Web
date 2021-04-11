package com.magician.web.load;

import com.magician.web.MagicianWebConfig;
import com.magician.web.core.annotation.Interceptor;
import com.magician.web.core.annotation.Route;
import com.magician.web.core.cache.MagicianCacheManager;
import com.magician.web.core.constant.MagicianWebConstant;
import com.magician.web.core.model.InterceptorModel;
import com.magician.web.core.model.RouteModel;
import com.magician.web.core.util.MatchUtil;
import com.magician.web.core.util.ScanUtil;
import io.magician.tcp.http.request.MagicianRequest;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 加载接口
 */
public class ApiLoad {

    /**
     * 加载接口
     * @throws Exception
     */
    public static void load() throws Exception {
        Set<String> scanClassList = MagicianCacheManager.getScanClassList();
        if(scanClassList != null){
            /* 如果他不为空说明已经执行过扫描了，所以直接断掉，不需要做重复的东西 */
            return;
        }

        /* 扫描包下的类 */
        scanClassList = ScanUtil.scanClassList(MagicianWebConfig.getScanPath());

        /* 筛选并创建拦截器和接口 */
        Map<String, InterceptorModel> interceptorModelMap = scanInterceptor(scanClassList);
        scanRoute(scanClassList);

        /* 将拦截器跟route比对后，分类存放 */
        Map<String, RouteModel> routeModelMap = MagicianCacheManager.getRouteMap();
        if(routeModelMap != null && routeModelMap.size() > 0){
            for(String routePath : routeModelMap.keySet()){
                for(String interPattern : interceptorModelMap.keySet()) {
                    if (MatchUtil.isMatch(interPattern, routePath)) {
                        MagicianCacheManager.setInterceptorMap(routePath, interceptorModelMap.get(interPattern));
                    }
                }
            }
        }
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
            interceptorModel.setObject(cls.getDeclaredConstructor().newInstance());
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

                    RouteModel routeModel = new RouteModel();
                    routeModel.setCls(cls);
                    routeModel.setMethod(method);
                    routeModel.setReqMethods(methodRoute.requestMethod());
                    routeModel.setRoute(getRoute(methodRoute, clsRoute));
                    routeModel.setObject(cls.getDeclaredConstructor().newInstance());

                    routeModelMap.put(routeModel.getRoute(), routeModel);
                }
            }
        }
        MagicianCacheManager.saveRouteMap(routeModelMap);
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
