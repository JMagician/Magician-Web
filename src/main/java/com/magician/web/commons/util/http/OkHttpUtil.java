package com.magician.web.commons.util.http;

import com.magician.web.commons.util.JSONUtil;
import com.magician.web.commons.util.http.model.FormDataParam;
import com.magician.web.mvc.core.constant.ReqMethod;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * okhttp工具类
 */
public class OkHttpUtil {

    private static OkHttpClient okHttpClient;

    static {
        okHttpClient = OKHttpClientBuilder
                .buildOKHttpClient()
                .build();
    }

    /**
     * 发送Json请求
     * @param url
     * @param param
     * @return
     * @throws IOException
     */
    public static Response requestBody(String url, String method, Map<String, String> header, Object param) throws Exception {
        method = method.toUpperCase();
        if (method.equals(ReqMethod.GET.getCode())) {
            throw new Exception("本方法不支持用GET方式发起请求， 你可以尝试使用 OkHttpUtil.get()");
        }

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(header))
                .method(method.toUpperCase(), RequestBody.create(JSONUtil.toJSONString(param), mediaType))
                .build();

        Call call = okHttpClient
                .newCall(request);

        return call.execute();
    }

    /**
     * 发起 formData请求
     * @param url
     * @param method
     * @param header
     * @param param
     * @return
     * @throws Exception
     */
    public  static Response requestFormData(String url, String method, Map<String, String> header, List<FormDataParam> param) throws Exception {
        method = method.toUpperCase();
        if (method.equals(ReqMethod.GET.getCode())) {
            throw new Exception("本方法不支持用GET方式发起请求， 你可以尝试使用 OkHttpUtil.get()");
        }

        MediaType formData = MediaType.parse("multipart/form-data");

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(formData);

        for (FormDataParam formDataParam : param) {
            if (formDataParam.isFile()) {
                RequestBody fileBody = RequestBody.create(formDataParam.getFile(), MediaType.parse("application/octet-stream"));
                builder.addFormDataPart(formDataParam.getName(), formDataParam.getFileName(), fileBody);
            } else {
                if (formDataParam.getValue() instanceof String[]) {
                    String[] valStr = (String[]) formDataParam.getValue();
                    for (String str : valStr) {
                        builder.addFormDataPart(formDataParam.getName(), str);
                    }
                } else {
                    builder.addFormDataPart(formDataParam.getName(), formDataParam.getValue().toString());
                }
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(header))
                .method(method, builder.build())
                .build();

        Call call = okHttpClient
                .newCall(request);

        return call.execute();
    }

    /**
     * 发起get请求
     * @param url
     * @param param
     * @return
     * @throws Exception
     */
    public static Response get(String url, Map<String, String> header, Object param) throws Exception {
        StringBuilder urlBuilder = new StringBuilder(url);
        if(param != null){
            urlBuilder.append("?");

            boolean first = true;
            Map<String, Object> paramMap = JSONUtil.toMap(param);
            for(String key : paramMap.keySet()){
                if(first == false){
                    urlBuilder.append("&");
                }

                urlBuilder.append(key);
                urlBuilder.append("=");
                urlBuilder.append(paramMap.get(key));

                first = false;
            }
        }

        Request request = new Request.Builder()
                .url(urlBuilder.toString())
                .headers(Headers.of(header))
                .get()
                .build();

        Call call = okHttpClient
                .newCall(request);

        return call.execute();
    }
}
