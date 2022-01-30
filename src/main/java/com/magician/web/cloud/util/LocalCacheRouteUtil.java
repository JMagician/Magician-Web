package com.magician.web.cloud.util;

import com.magician.web.cloud.load.cache.LocalCacheRoute;
import com.magician.web.cloud.load.cache.LocalCacheRouteManager;
import com.magician.web.cloud.load.cache.LocalCacheRouteModel;
import com.magician.web.cloud.route.model.CommunicationParamModel;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class LocalCacheRouteUtil {

    /**
     * 随机获取一个url作为下一次 数据交换的 节点
     */
    private static Random random = new Random();

    /**
     * 将 communicationParamModel 插入到 LocalCacheRouteMap
     * @param communicationParamModel
     */
    public static void CommunicationParamModelInsertLocalCacheRouteMap(CommunicationParamModel communicationParamModel){
        boolean isThisServer = false;
        int nextInt = -1;

        Map<String, Map<String, LocalCacheRoute>> cacheRouteMap = communicationParamModel.getCacheRouteMap();
        if(cacheRouteMap == null){
            return;
        }

        // 随机取出一个服务
        int randomServer = random.nextInt(cacheRouteMap.size() - 1);

        int j = 0;
        for(String key : cacheRouteMap.keySet()){
            Map<String, LocalCacheRoute> routeMap = cacheRouteMap.get(key);
            if(routeMap == null){
                j++;
                continue;
            }

            // 如果当前服务就是随机出来的那个服务，就标记一下
            if(randomServer == j){
                isThisServer = true;
            }

            for(String path : routeMap.keySet()){
                LocalCacheRoute localCacheRoute = routeMap.get(path);
                List<LocalCacheRouteModel> localCacheRouteModels = localCacheRoute.getLocalCacheRouteModels();
                if(localCacheRouteModels == null || localCacheRouteModels.size() < 1){
                    continue;
                }

                // 如果当前服务被标记了，就从本服务的集群中 随机出一个节点，作为下次数据交互的节点
                if(isThisServer){
                    nextInt = random.nextInt(localCacheRouteModels.size() - 1);
                }

                for(int i=0; i < localCacheRouteModels.size(); i++){
                    LocalCacheRouteModel localCacheRouteModel = localCacheRouteModels.get(i);

                    LocalCacheRouteManager.add(localCacheRouteModel.getServerName(), localCacheRouteModel.getPath(), localCacheRouteModel);
                    if(i == nextInt){
                        LocalCacheRouteManager.setConnectionHostName(localCacheRouteModel.getServerUrl());
                    }
                }
            }

            isThisServer = false;
            j++;
        }
    }
}
