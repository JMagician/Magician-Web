package com.magician.web.execute;

import com.magician.web.core.cache.MagicianWebCacheManager;
import com.magician.web.core.constant.MagicianWebConstant;
import com.magician.web.core.constant.ReqMethod;
import com.magician.web.core.interceptor.MagicianInterceptor;
import com.magician.web.core.model.InterceptorModel;
import com.magician.web.core.model.RouteModel;
import com.magician.web.core.util.JSONUtil;
import com.magician.web.core.util.MesUtil;
import com.magician.web.core.util.ParamsCheckUtil;
import com.magician.web.execute.model.ResponseInputStream;
import io.magician.application.request.MagicianRequest;
import io.netty.handler.codec.http.HttpMethod;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 接口执行
 */
public class ApiExecute {

    /**
     * 执行接口
     * @param request
     * @return
     * @throws Exception
     */
    public static void execute(MagicianRequest request) throws Exception {
        Map<String, RouteModel> routeModelMap = MagicianWebCacheManager.getRouteMap();

        String url = getUri(request.getUrl());
        RouteModel routeModel = routeModelMap.get(url);

        /* 校验请求是否符合规则 */
        String checkRouteResult = check(routeModel, request, url);
        if(checkRouteResult != null){
            sendText(request, checkRouteResult);
            return;
        }

        Method method = routeModel.getMethod();
        Object[] params = BuildParams.builder(method, request);

        /* 校验传参 */
        String checkResult = ParamsCheckUtil.checkParam(params, url);
        if(checkResult != null){
            sendText(request, checkResult);
            return;
        }

        /* 获取这个路由对应的拦截器 */
        List<InterceptorModel> interceptorModelList = InterceptorExecute.getInterceptorModelList(url);

        /* 执行拦截器的before */
        Object interResult = InterceptorExecute.before(interceptorModelList, request);
        if(!MagicianInterceptor.SUCCESS.equals(String.valueOf(interResult))){
            sendText(request, String.valueOf(interResult));
            return;
        }

        /* 执行Controller */
        Object result = null;
        if(params == null){
            result = method.invoke(routeModel.getObject());
        } else {
            result = method.invoke(routeModel.getObject(), params);
        }

        /* 执行拦截器的after */
        interResult = InterceptorExecute.after(interceptorModelList, request, result);
        if(!MagicianInterceptor.SUCCESS.equals(String.valueOf(interResult))){
            sendText(request, String.valueOf(interResult));
            return;
        }

        /* 如果返回的是个流，就直接响应流 */
        if(result instanceof ResponseInputStream){
            ResponseInputStream inputStream = (ResponseInputStream)result;
            request.getResponse().sendStream(inputStream.getName(), inputStream.getBytes());
            return;
        }

        /* 如果返回值不不是流，则直接响应 */
        sendText(request, JSONUtil.toJSONString(result));
    }

    /**
     * 响应文字
     * @param request
     * @param text
     */
    private static void sendText(MagicianRequest request, String text) throws Exception {
        request.getResponse()
                .sendJson(text);
    }

    /**
     * 校验请求是否符合规则
     * @param routeModel
     * @param request
     * @param url
     * @return
     */
    private static String check(RouteModel routeModel, MagicianRequest request, String url){
        if(routeModel == null) {
            return MesUtil.getMes(400,"没有找到这个接口[" + url + "]");
        }

        HttpMethod strMethod = request.getMethod();
        /* 如果请求方式是options，那就说明这是一个试探性的请求，直接响应即可 */
        if(strMethod.equals(HttpMethod.OPTIONS)){
            return MagicianWebConstant.OPTIONS;
        }

        /* 接口上设置的请求方式 只要有一个匹配就校验通过 */
        for(ReqMethod reqMethod : routeModel.getReqMethods()){
            if(reqMethod.getCode().equals(strMethod.name())){
                return null;
            }
        }

        return MesUtil.getMes(400,"接口的请求方式为[" + JSONUtil.toJSONString(routeModel.getReqMethods()) + "]");
    }

    /**
     * 获取纯净的uri
     * @param uri
     * @return
     */
    private static String getUri(String uri){
        if(!uri.startsWith("/")){
            uri = "/"+uri;
        }
        int endIndex = uri.length();
        if(uri.lastIndexOf("?") > -1){
            endIndex = uri.lastIndexOf("?");
        }
        return uri.substring(0, endIndex).toUpperCase();
    }
}
