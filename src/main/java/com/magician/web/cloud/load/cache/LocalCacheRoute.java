package com.magician.web.cloud.load.cache;

import java.util.ArrayList;
import java.util.List;

public class LocalCacheRoute {

    private int index = 0;

    private long removeTime = 0;

    private List<LocalCacheRouteModel> localCacheRouteModels = new ArrayList<>();

    public synchronized void add(LocalCacheRouteModel localCacheRouteModel){
        localCacheRouteModel.setCreateTime(System.currentTimeMillis());
        localCacheRouteModels.add(localCacheRouteModel);
    }

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

    private void clear(){
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
