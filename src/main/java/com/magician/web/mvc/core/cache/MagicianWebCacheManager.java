package com.magician.web.mvc.core.cache;

import com.magician.web.mvc.core.model.InterceptorModel;
import com.magician.web.mvc.core.model.RouteModel;

import java.util.*;

/**
 * Cache management
 */
public class MagicianWebCacheManager {

    /**
     * an identified interface
     */
    private static Map<String, RouteModel> routeMap;
    /**
     * Interceptors that have been identified and classified according to route
     */
    private static Map<String, List<InterceptorModel>> interceptorMap = new HashMap<>();

    public static Map<String, RouteModel> getRouteMap() {
        return routeMap;
    }

    public static void saveRouteMap(Map<String, RouteModel> routeMap) {
        MagicianWebCacheManager.routeMap = routeMap;
    }

    public static Map<String, List<InterceptorModel>> getInterceptorMap() {
        return interceptorMap;
    }

    public static void setInterceptorMap(String route, InterceptorModel interceptorModel) {
        List<InterceptorModel> interceptorModelList = interceptorMap.get(route);
        if(interceptorModelList == null){
            interceptorModelList = new ArrayList<>();
        }

        for(InterceptorModel inter : interceptorModelList){
            if(inter.getCls().equals(interceptorModel.getCls())){
                return;
            }
        }

        interceptorModelList.add(interceptorModel);
        interceptorMap.put(route, interceptorModelList);
    }
}
