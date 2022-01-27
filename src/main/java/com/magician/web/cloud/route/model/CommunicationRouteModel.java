package com.magician.web.cloud.route.model;

import com.magician.web.cloud.load.cache.LocalCacheRouteModel;

import java.util.List;

public class CommunicationRouteModel {
    private String serverName;

    private List<LocalCacheRouteModel> localCacheRouteModelList;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public List<LocalCacheRouteModel> getLocalCacheRouteModelList() {
        return localCacheRouteModelList;
    }

    public void setLocalCacheRouteModelList(List<LocalCacheRouteModel> localCacheRouteModelList) {
        this.localCacheRouteModelList = localCacheRouteModelList;
    }
}
