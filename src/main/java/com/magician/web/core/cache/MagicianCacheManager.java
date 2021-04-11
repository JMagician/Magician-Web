package com.magician.web.core.cache;

import com.magician.web.core.model.RouteModel;

import java.util.Map;
import java.util.Set;

public class MagicianCacheManager {
    /**
     * 扫描出来的类
     */
    private static Set<String> classList;
    /**
     * 识别出来的接口
     */
    private static Map<String, RouteModel> routeMap;

    public static Set<String> getScanClassList() {
        return classList;
    }

    public static void saveScanClassList(Set<String> classList) {
        MagicianCacheManager.classList = classList;
    }

    public static Map<String, RouteModel> getRouteMap() {
        return routeMap;
    }

    public static void saveRouteMap(Map<String, RouteModel> routeMap) {
        MagicianCacheManager.routeMap = routeMap;
    }
}
