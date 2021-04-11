package com.magician.web.load;

import com.magician.web.MagicianWebConfig;
import com.magician.web.core.annotation.Route;
import com.magician.web.core.cache.MagicianCacheManager;
import com.magician.web.core.model.RouteModel;
import com.magician.web.core.util.ScanUtil;

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
        List<String> packages = new ArrayList<>();
        packages.add(MagicianWebConfig.getScanPath());
        scanClassList = ScanUtil.scanClassList(packages);

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
