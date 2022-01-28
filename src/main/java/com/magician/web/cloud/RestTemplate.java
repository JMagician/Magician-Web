package com.magician.web.cloud;

import com.magician.web.cloud.load.cache.LocalCacheRoute;
import com.magician.web.cloud.load.cache.LocalCacheRouteManager;
import com.magician.web.cloud.load.cache.LocalCacheRouteModel;
import com.magician.web.commons.util.JSONUtil;
import com.magician.web.commons.util.http.OkHttpUtil;
import com.magician.web.commons.util.http.model.FormDataParam;
import com.magician.web.mvc.core.constant.ReqMethod;
import okhttp3.Response;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * 请求微服务接口的工具类
 */
public class RestTemplate {

    /**
     * 请求微服务接口，返回JSON格式的数据，并通过泛型转化成自己指定的类型
     * @param serverName
     * @param routePath
     * @param param
     * @param cls
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T sendResultJson(String serverName, String routePath, Object param, Class<T> cls) throws Exception {
        Response response = request(serverName, routePath, param);
        return returnMessage(response, cls);
    }

    /**
     * 请求微服务接口，发送formData格式的数据，返回JSON格式的数据，并通过泛型转化成自己指定的类型
     * @param serverName
     * @param routePath
     * @param param
     * @param cls
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T sendUpload(String serverName, String routePath, List<FormDataParam> param, Class<T> cls) throws Exception {
        LocalCacheRouteModel localCacheRouteModel = getLocalCacheRouteModel(serverName, routePath);
        if(localCacheRouteModel == null){
            throw new Exception("访问的接口不存在，serverName:[" + serverName + "], routePath:[" + routePath + "]");
        }

        Response response = OkHttpUtil.requestFormData(localCacheRouteModel.getUrl(), localCacheRouteModel.getMethod(), new HashMap<>(), param);
        return returnMessage(response, cls);
    }

    /**
     * 请求微服务接口，返回流数据，常用于请求另一个服务的下载文件接口
     * @param serverName
     * @param routePath
     * @param param
     * @return
     * @throws Exception
     */
    public static InputStream sendResultStream(String serverName, String routePath, Object param) throws Exception {
        Response response = request(serverName, routePath, param);
        if(response == null){
            throw new Exception("请求出现异常");
        }
        if(response.code() != 200){
            throw new Exception("请求出现异常，状态码:" + response.code());
        }

        return response.body().byteStream();
    }

    /**
     * 获取接口，发起请求
     * @param serverName
     * @param routePath
     * @param param
     * @return
     * @throws Exception
     */
    private static Response request(String serverName, String routePath, Object param) throws Exception {

        LocalCacheRouteModel localCacheRouteModel = getLocalCacheRouteModel(serverName, routePath);
        if(localCacheRouteModel == null){
            throw new Exception("访问的接口不存在，serverName:[" + serverName + "], routePath:[" + routePath + "]");
        }

        Response response = null;

        if(ReqMethod.GET.getCode().equals(localCacheRouteModel.getMethod())){
            response = OkHttpUtil.get(localCacheRouteModel.getUrl(), new HashMap<>(), param);
        } else {
            response = OkHttpUtil.requestBody(localCacheRouteModel.getUrl(), localCacheRouteModel.getMethod(), new HashMap<>(), param);
        }

        return response;
    }

    /**
     * 获取指定的服务接口
     * @param serverName
     * @param routePath
     * @return
     */
    private static LocalCacheRouteModel getLocalCacheRouteModel(String serverName, String routePath) throws Exception {
        LocalCacheRoute localCacheRoute = LocalCacheRouteManager.get(serverName, routePath);
        if(localCacheRoute == null){
            throw new Exception("访问的接口不存在，serverName:[" + serverName + "], routePath:[" + routePath + "]");
        }
        return localCacheRoute.get();
    }

    /**
     * 将返回JSON格式的数据，通过泛型转化成自己指定的类型
     * @param response
     * @param cls
     * @param <T>
     * @return
     * @throws Exception
     */
    private static <T> T returnMessage(Response response, Class<T> cls) throws Exception {
        if(response == null){
            throw new Exception("请求出现异常");
        }
        if(response.code() != 200){
            throw new Exception("请求出现异常，状态码:" + response.code());
        }

        String result = response.body().string();
        if(cls.equals(String.class)){
            return (T)result;
        }

        return JSONUtil.toJavaObject(result, cls);
    }
}
