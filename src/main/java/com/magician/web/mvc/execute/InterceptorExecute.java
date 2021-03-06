package com.magician.web.mvc.execute;

import com.magician.web.mvc.core.cache.MagicianWebCacheManager;
import com.magician.web.mvc.core.interceptor.MagicianInterceptor;
import com.magician.web.mvc.core.model.InterceptorModel;
import io.magician.application.request.MagicianRequest;

import java.util.List;
import java.util.Map;

/**
 * 执行拦截器
 */
public class InterceptorExecute {

    private static  Map<String, List<InterceptorModel>> interMap;

    /**
     * 获取这个路由对应的拦截器
     * @param route
     * @return
     */
    public static List<InterceptorModel> getInterceptorModelList(String route){
        if(interMap == null){
            interMap = MagicianWebCacheManager.getInterceptorMap();
        }
        return interMap.get(route);
    }

    /**
     * 执行拦截器的before
     * @param interceptorModelList
     * @param request
     * @return
     * @throws Exception
     */
    public static Object before(List<InterceptorModel> interceptorModelList, MagicianRequest request) throws Exception {
        if(interceptorModelList == null || interceptorModelList.size() < 1){
            return MagicianInterceptor.SUCCESS;
        }

        for(InterceptorModel interceptorModel : interceptorModelList){
            Object result = interceptorModel.getMagicianInterceptor().before(request);
            if(!MagicianInterceptor.SUCCESS.equals(String.valueOf(result))){
                return result;
            }
        }

        return MagicianInterceptor.SUCCESS;
    }

    /**
     * 执行拦截器的after
     * @param interceptorModelList
     * @param request
     * @param apiResult
     * @return
     * @throws Exception
     */
    public static Object after(List<InterceptorModel> interceptorModelList, MagicianRequest request, Object apiResult) throws Exception {
        if(interceptorModelList == null || interceptorModelList.size() < 1){
            return MagicianInterceptor.SUCCESS;
        }

        for(InterceptorModel interceptorModel : interceptorModelList){
            Object result = interceptorModel.getMagicianInterceptor().after(request, apiResult);
            if(!MagicianInterceptor.SUCCESS.equals(String.valueOf(result))){
                return result;
            }
        }

        return MagicianInterceptor.SUCCESS;
    }
}
