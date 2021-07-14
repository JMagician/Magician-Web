package com.magician.web.core.cache;

import com.magician.web.core.model.InterceptorModel;
import com.magician.web.core.model.RouteModel;

import java.util.*;

/**
 * 缓存管理
 */
public class MagicianWebCacheManager {

    /**
     * 识别出来的接口
     */
    private static Map<String, RouteModel> routeMap;
    /**
     * 识别出来，并根据route分类号的拦截器
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
