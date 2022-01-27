package com.magician.web.cloud.route;

import com.magician.web.cloud.config.CloudConfig;
import com.magician.web.cloud.config.Constants;
import com.magician.web.cloud.load.cache.LocalCacheRoute;
import com.magician.web.cloud.load.cache.LocalCacheRouteManager;
import com.magician.web.cloud.load.cache.LocalCacheRouteModel;
import com.magician.web.cloud.route.model.CommunicationParamModel;
import com.magician.web.cloud.util.LocalCacheRouteUtil;
import com.magician.web.commons.util.MsgUtil;
import com.magician.web.commons.util.StringUtil;
import com.magician.web.mvc.core.annotation.Route;
import com.magician.web.mvc.core.constant.ReqMethod;
import io.magician.application.request.MagicianRequest;

import java.util.List;
import java.util.Map;

@Route(Constants.COMMUNICATION_ROUTE)
public class CommunicationRoute {

    /**
     * 接收其他节点的数据，并将自己的数据返回给其他节点
     * @param communicationParamModel
     * @return
     */
    @Route(value = Constants.DATA_EXCHANGE, requestMethod = ReqMethod.POST)
    public CommunicationParamModel dataExchange(CommunicationParamModel communicationParamModel){

        LocalCacheRouteUtil.CommunicationParamModelInsertLocalCacheRouteMap(communicationParamModel);

        CommunicationParamModel communicationParam = new CommunicationParamModel();
        communicationParam.setCacheRouteMap(LocalCacheRouteManager.getLocalCacheRouteMaps());
        return communicationParam;
    }

    /**
     * 查询本节点的所有 接口缓存列表（理论上可以查到整个微服务的所有接口列表）
     */
    @Route(value = Constants.SELECT_CLOUD_ROUTE_LIST, requestMethod = ReqMethod.GET)
    public Object selectCloudRouteList(MagicianRequest request){
        String secretKey = request.getParam("secretKey");
        if(StringUtil.isNull(secretKey) || !secretKey.equals(CloudConfig.getSecretKey())){
            return MsgUtil.getMsg(1128, "请输入正确的秘钥");
        }

        return LocalCacheRouteManager.getLocalCacheRouteMaps();
    }
}
