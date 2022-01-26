package com.magician.web.cloud;

import com.magician.web.cloud.load.cache.LocalCacheRoute;
import com.magician.web.cloud.load.cache.LocalCacheRouteManager;
import com.magician.web.cloud.load.cache.LocalCacheRouteModel;
import com.magician.web.commons.util.http.OkHttpUtil;
import com.magician.web.commons.util.http.model.FormDataParam;
import com.magician.web.mvc.core.constant.ReqMethod;
import okhttp3.Response;

import java.util.HashMap;
import java.util.List;

public class RestTemplate {

    public static <T> T sendJson(String serverName, String route, Object param, Class<T> cls) throws Exception {
        Response response = request(serverName, route, param);

        return null;
    }

    public static <T> T sendStream(String serverName, String route, Object param, Class<T> cls) throws Exception {
        Response response = request(serverName, route, param);

        return null;
    }

    public static <T> T sendUpload(String serverName, String route, List<FormDataParam> param, Class<T> cls) throws Exception {
        LocalCacheRoute localCacheRoute = LocalCacheRouteManager.get(serverName, route);
        LocalCacheRouteModel localCacheRouteModel = localCacheRoute.get();
        if(localCacheRouteModel == null){
            return null;
        }

        Response response = OkHttpUtil.requestFormData(localCacheRouteModel.getUrl(), localCacheRouteModel.getMethod(), new HashMap<>(), param);
        return null;
    }


    private static Response request(String serverName, String route, Object param) throws Exception {
        LocalCacheRoute localCacheRoute = LocalCacheRouteManager.get(serverName, route);
        LocalCacheRouteModel localCacheRouteModel = localCacheRoute.get();
        if(localCacheRouteModel == null){
            return null;
        }

        Response response = null;

        if(ReqMethod.GET.getCode().equals(localCacheRouteModel.getMethod())){
            response = OkHttpUtil.get(localCacheRouteModel.getUrl(), new HashMap<>(), param);
        } else {
            response = OkHttpUtil.requestBody(localCacheRouteModel.getUrl(), localCacheRouteModel.getMethod(), new HashMap<>(), param);
        }

        return response;
    }
}
