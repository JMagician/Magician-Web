package com.magician.web.cloud.load.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一个服务的 接口集合
 */
public class LocalCacheRoute {

    /**
     * 下标，用来做轮询的
     */
    private int index = 0;

    /**
     * 最后删除时间，用来实现 删除频率控制
     */
    private long removeTime = 0;

    /**
     * 接口集合
     */
    private List<LocalCacheRouteModel> localCacheRouteModels = new ArrayList<>();

    public List<LocalCacheRouteModel> getLocalCacheRouteModels() {
        return localCacheRouteModels;
    }

    /**
     * 添加一个接口
     * @param localCacheRouteModel
     */
    public synchronized void add(LocalCacheRouteModel localCacheRouteModel) {
        for (LocalCacheRouteModel cacheRouteModel : localCacheRouteModels) {
            if (cacheRouteModel.getUrl().equals(localCacheRouteModel.getUrl()) && cacheRouteModel.getServerName().equals(localCacheRouteModel.getServerName())) {
                cacheRouteModel.setCreateTime(System.currentTimeMillis());
                return;
            }
        }
        localCacheRouteModel.setCreateTime(System.currentTimeMillis());
        localCacheRouteModels.add(localCacheRouteModel);
    }

    /**
     * 获取一个 服务接口
     * @return
     */
    public synchronized LocalCacheRouteModel get(){
        clear();
        if(localCacheRouteModels == null || localCacheRouteModels.size() < 1){
            return null;
        }

        if(localCacheRouteModels.size() - 1 >= index){
            index = 0;
        } else {
            index++;
        }

        return localCacheRouteModels.get(index);
    }

    /**
     * 清理过期的接口
     */
    private void clear(){
        // 1秒清理一次
        if(removeTime > 0 && (System.currentTimeMillis() - removeTime) < 1000){
            return;
        }

        List<LocalCacheRouteModel> doRemove = new ArrayList<>();

        for(LocalCacheRouteModel localCacheRouteModel : localCacheRouteModels){
            if((System.currentTimeMillis() - localCacheRouteModel.getCreateTime()) > 5000){
                doRemove.add(localCacheRouteModel);
            }
        }

        if(doRemove.size() > 0){
            localCacheRouteModels.removeAll(doRemove);
        }

        removeTime = System.currentTimeMillis();
    }
}
