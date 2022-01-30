package com.magician.web.cloud.load.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地接口列表缓存管理
 */
public class LocalCacheRouteManager {

    /**
     * 本地接口列表缓存
     */
    private static Map<String, Map<String, LocalCacheRoute>>  localCacheRouteMaps = new ConcurrentHashMap<>();

    /**
     * 数据交换的 节点地址
     */
    private static String connectionHostName;

    public static Map<String, Map<String, LocalCacheRoute>> getLocalCacheRouteMaps() {
        return localCacheRouteMaps;
    }

    public static String getConnectionHostName() {
        return connectionHostName;
    }

    public static void setConnectionHostName(String connectionHostName) {
        LocalCacheRouteManager.connectionHostName = connectionHostName;
    }

    /**
     * 根据serverName和路由path 获取该节点集群中所有path对应的url
     * @param serverName
     * @param path
     * @return
     */
    public static LocalCacheRoute get(String serverName, String path){
        Map<String, LocalCacheRoute> localCacheRouteMap = localCacheRouteMaps.get(serverName);
        if(localCacheRouteMap == null){
            localCacheRouteMap = new ConcurrentHashMap<>();
            localCacheRouteMaps.put(serverName, localCacheRouteMap);
        }

        return localCacheRouteMap.get(path);
    }

    /**
     * 往缓存中添加一个接口
     * @param serverName
     * @param path
     * @param localCacheRouteModel
     */
    public static void add(String serverName, String path, LocalCacheRouteModel localCacheRouteModel){
        LocalCacheRoute localCacheRoute = get(serverName, path);
        if(localCacheRoute == null){
            localCacheRoute = new LocalCacheRoute();
        }
        localCacheRoute.add(localCacheRouteModel);
        localCacheRouteMaps.get(serverName).put(path, localCacheRoute);
    }
}
