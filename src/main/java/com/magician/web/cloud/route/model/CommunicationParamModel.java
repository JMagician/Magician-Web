package com.magician.web.cloud.route.model;

import com.magician.web.cloud.load.cache.LocalCacheRoute;

import java.util.Map;

public class CommunicationParamModel {

   private Map<String, Map<String, LocalCacheRoute>> cacheRouteMap;

    public Map<String, Map<String, LocalCacheRoute>> getCacheRouteMap() {
        return cacheRouteMap;
    }

    public void setCacheRouteMap(Map<String, Map<String, LocalCacheRoute>> cacheRouteMap) {
        this.cacheRouteMap = cacheRouteMap;
    }
}
