package com.magician.web.mvc.execute;

import com.magician.web.mvc.core.cache.MagicianWebCacheManager;
import com.magician.web.mvc.core.constant.MagicianWebConstant;
import com.magician.web.mvc.core.constant.ReqMethod;
import com.magician.web.mvc.core.interceptor.MagicianInterceptor;
import com.magician.web.mvc.core.model.InterceptorModel;
import com.magician.web.mvc.core.model.RouteModel;
import com.magician.web.commons.util.JSONUtil;
import com.magician.web.commons.util.MsgUtil;
import com.magician.web.commons.util.ParamsCheckUtil;
import com.magician.web.mvc.execute.model.ResponseInputStream;
import io.magician.application.request.MagicianRequest;
import io.netty.handler.codec.http.HttpMethod;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * execute routing
 */
public class MvcExecute {

    /**
     * execute routing
     * @param request
     * @return
     * @throws Exception
     */
    public static void execute(MagicianRequest request) throws Exception {
        Map<String, RouteModel> routeModelMap = MagicianWebCacheManager.getRouteMap();

        String url = getUri(request.getUrl());
        RouteModel routeModel = routeModelMap.get(url);

        /* Check whether the request conforms to the rules */
        String checkRouteResult = check(routeModel, request, url);
        if(checkRouteResult != null){
            request.getResponse().sendJson(checkRouteResult);
            return;
        }

        Method method = routeModel.getMethod();
        Object[] params = BuildParams.builder(method, request);

        /* Check the parameters */
        String checkResult = ParamsCheckUtil.checkParam(params, url);
        if(checkResult != null){
            request.getResponse().sendJson(checkResult);
            return;
        }

        /* Get the interceptor corresponding to this route */
        List<InterceptorModel> interceptorModelList = InterceptorExecute.getInterceptorModelList(url);

        /* Execute the before method of the interceptor */
        Object interResult = InterceptorExecute.before(interceptorModelList, request);
        if(!MagicianInterceptor.SUCCESS.equals(String.valueOf(interResult))){
            request.getResponse().sendJson(String.valueOf(interResult));
            return;
        }

        /* Execute Controller */
        Object result = null;
        if(params == null){
            result = method.invoke(routeModel.getObject());
        } else {
            result = method.invoke(routeModel.getObject(), params);
        }

        /* Execute the after method of the interceptor */
        interResult = InterceptorExecute.after(interceptorModelList, request, result);
        if(!MagicianInterceptor.SUCCESS.equals(String.valueOf(interResult))){
            request.getResponse().sendJson(String.valueOf(interResult));
            return;
        }

        // If it returns null, it means that the controller has already responded, so no response is required
        if(result == null){
            return;
        }

        /* If it returns ResponseInputStream, it responds directly to the stream */
        if(result instanceof ResponseInputStream){
            ResponseInputStream inputStream = (ResponseInputStream)result;
            request.getResponse().sendStream(inputStream.getName(), inputStream.getBytes());
            return;
        }

        /* If the type of the return value is not ResponseInputStream, respond directly */
        request.getResponse().sendJson(JSONUtil.toJSONString(result));
    }

    /**
     * Check whether the request conforms to the rules
     * @param routeModel
     * @param request
     * @param url
     * @return
     */
    private static String check(RouteModel routeModel, MagicianRequest request, String url){
        if(routeModel == null) {
            return MsgUtil.getMsg(400,"Could not find this route [" + url + "]");
        }

        HttpMethod strMethod = request.getMethod();
        /* If the request method is options, it means that this is a tentative request, and you can respond directly */
        if(strMethod.equals(HttpMethod.OPTIONS)){
            return MagicianWebConstant.OPTIONS;
        }

        /* The request method set on the route will pass as long as there is a match */
        for(ReqMethod reqMethod : routeModel.getReqMethods()){
            if(reqMethod.getCode().equals(strMethod.name())){
                return null;
            }
        }

        return MsgUtil.getMsg(400,"The request method accepted by the route is[" + JSONUtil.toJSONString(routeModel.getReqMethods()) + "]");
    }

    /**
     * get pure uri
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
