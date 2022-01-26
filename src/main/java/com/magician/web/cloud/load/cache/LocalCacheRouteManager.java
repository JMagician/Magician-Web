package com.magician.web.cloud.load.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalCacheRouteManager {

    private static Map<String, Map<String, LocalCacheRoute>>  localCacheRouteMaps = new ConcurrentHashMap<>();

    public static LocalCacheRoute get(String serverName, String path){
        Map<String, LocalCacheRoute> localCacheRouteMap = localCacheRouteMaps.get(serverName);
        if(localCacheRouteMap == null){
            localCacheRouteMap = new ConcurrentHashMap<>();
            localCacheRouteMaps.put(serverName, localCacheRouteMap);
        }

        return localCacheRouteMap.get(path);
    }

    public static void add(String serverName, String path, LocalCacheRouteModel localCacheRouteModel){
        LocalCacheRoute localCacheRoute = get(serverName, path);
        if(localCacheRoute == null){
            localCacheRoute = new LocalCacheRoute();
        }
        localCacheRoute.add(localCacheRouteModel);
        localCacheRouteMaps.get(serverName).put(path, localCacheRoute);
    }
}
