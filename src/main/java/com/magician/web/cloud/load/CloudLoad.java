package com.magician.web.cloud.load;

import com.magician.web.cloud.config.CloudConfig;
import com.magician.web.cloud.config.Constants;
import com.magician.web.cloud.load.cache.LocalCacheRouteManager;
import com.magician.web.cloud.load.cache.LocalCacheRouteModel;
import com.magician.web.cloud.route.model.CommunicationParamModel;
import com.magician.web.cloud.util.LocalCacheRouteUtil;
import com.magician.web.commons.util.JSONUtil;
import com.magician.web.commons.util.StringUtil;
import com.magician.web.commons.util.http.OkHttpUtil;
import com.magician.web.mvc.core.cache.MagicianWebCacheManager;
import com.magician.web.mvc.core.constant.ReqMethod;
import com.magician.web.mvc.core.model.RouteModel;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 启动时 需要执行的一些逻辑
 */
public class CloudLoad {

    private static Logger logger = LoggerFactory.getLogger(CloudLoad.class);

    /**
     * 实现 跟其他节点的连接，以及接口列表数据交互
     */
    public static void communication() {
        try {
            CommunicationParamModel communicationParamModel = new CommunicationParamModel();
            communicationParamModel.setCacheRouteMap(LocalCacheRouteManager.getLocalCacheRouteMaps());

            Response response = OkHttpUtil.requestBody(getConnection() + Constants.COMMUNICATION_ROUTE + Constants.DATA_EXCHANGE,
                    ReqMethod.POST.getCode(),
                    new HashMap<>(),
                    communicationParamModel
            );

            if(response == null){
                return;
            }

            if(response.code() != 200){
                throw new Exception("状态码:" + response.code());
            }

            String result = response.body().string();
            communicationParamModel = JSONUtil.toJavaObject(result, CommunicationParamModel.class);

            LocalCacheRouteUtil.CommunicationParamModelInsertLocalCacheRouteMap(communicationParamModel);
        } catch (Exception e) {
            logger.error("跟其他节点数据交换出现异常", e);
        }
    }

    /**
     * 将自己的接口 初始化到 LocalCacheRouteMap
     */
    public static void initOwnerRouteInsertToLocalCacheRouteMap(){
        Map<String, RouteModel> routeMap = MagicianWebCacheManager.getRouteMap();

        for(String key : routeMap.keySet()){
            RouteModel routeModel = routeMap.get(key);

            String path = getPath(routeModel.getRoute());
            String url = getServerUrl(CloudConfig.getServerUrl());

            LocalCacheRouteModel localCacheRouteModel = new LocalCacheRouteModel();
            localCacheRouteModel.setCreateTime(System.currentTimeMillis());
            localCacheRouteModel.setMethod(routeModel.getReqMethods()[0].getCode());
            localCacheRouteModel.setPath(routeModel.getRoute());
            localCacheRouteModel.setUrl(url + path);
            localCacheRouteModel.setServerUrl(url);
            localCacheRouteModel.setServerName(CloudConfig.getServerName());

            LocalCacheRouteManager.add(CloudConfig.getServerName(), path, localCacheRouteModel);
        }
    }

    /**
     * 处理 path开头的斜杠
     * @param route
     * @return
     */
    private static String getPath(String route){
        if(StringUtil.isNull(route)){
            return null;
        }

        if(route.startsWith("/")){
            return route;
        }

        return "/" + route;
    }

    /**
     * 处理 serverUrl结尾的斜杠
     * @param serverUrl
     * @return
     */
    private static String getServerUrl(String serverUrl){
        if(serverUrl == null){
            return null;
        }

        if(serverUrl.endsWith("/")){
            return serverUrl.substring(0, serverUrl.length() - 1);
        }

        return serverUrl;
    }

    /**
     * 随机获取要交互的服务
     * @return
     */
    private static String getConnection(){
        if(StringUtil.isNull(LocalCacheRouteManager.getConnectionHostName())){
            return CloudConfig.getConnection();
        }
        return LocalCacheRouteManager.getConnectionHostName();
    }
}
