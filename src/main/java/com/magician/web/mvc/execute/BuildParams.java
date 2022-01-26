package com.magician.web.mvc.execute;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.magician.web.commons.constant.DataType;
import com.magician.web.commons.util.JSONUtil;
import com.magician.web.commons.util.ParamTypeUtil;
import io.magician.application.request.MagicianRequest;
import io.netty.handler.codec.http.multipart.MixedFileUpload;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 构建参数
 */
public class BuildParams {

    /**
     * 构建MarsApi的传参
     * @param method
     * @param request
     * @return
     * @throws Exception
     */
    public static Object[] builder(Method method, MagicianRequest request) throws Exception {
        try {
            Class requestClass = MagicianRequest.class;

            Class[] paramTypes = method.getParameterTypes();
            if (paramTypes == null || paramTypes.length < 1) {
                return null;
            }
            Object[] params = new Object[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                Class cls = paramTypes[i];
                if (requestClass.equals(cls)) {
                    params[i] = request;
                } else {
                    params[i] = getObject(cls, request);
                }
            }
            return params;
        } catch (Exception e) {
            throw new Exception("参数注入异常", e);
        }
    }

    /**
     * 构建参数对象
     * @param cls
     * @param request
     * @return
     * @throws Exception
     */
    private static Object getObject(Class cls, MagicianRequest request) throws Exception {
        /* 如果是Json传参，那就直接转成Java对象返回 */
        if(ParamTypeUtil.isJSON(request.getContentType())){
            String paramJson = request.getJsonParam();
            if(paramJson == null){
                return null;
            }
            return JSONUtil.toJavaObject(paramJson, cls);
        }

        /* 如果不是Json传参，那就用反射来处理 */
        Object obj = cls.getDeclaredConstructor().newInstance();
        Field[] fields = cls.getDeclaredFields();
        for(Field f : fields){
            boolean isFinal = Modifier.isFinal(f.getModifiers());
            if(isFinal){
                continue;
            }

            f.setAccessible(true);

            List<String> valList = request.getParams(f.getName());

            if(f.getType().equals(MixedFileUpload.class)){
                MixedFileUpload mixedFileUpload = request.getFile(f.getName());
                if (mixedFileUpload != null){
                    f.set(obj, mixedFileUpload);
                }
            } else if(f.getType().equals(MixedFileUpload[].class)){
                putMarsFileUploads(f,obj, request.getFiles(f.getName()));
            } else if(valList != null && valList.size() > 0){
                putAttr(f,obj,valList);
            }
        }
        return obj;
    }

    /**
     * 给参数赋值
     * @param field 字段
     * @param obj 对象
     * @throws Exception 异常
     */
    private static void putMarsFileUploads(Field field, Object obj,List<MixedFileUpload> marsFileUpLoadList) throws Exception{
        if (marsFileUpLoadList == null) {
            return;
        }
        MixedFileUpload[] marsFileUpLoads = new MixedFileUpload[marsFileUpLoadList.size()];
        int index = 0;
        for(MixedFileUpload item : marsFileUpLoadList){
            marsFileUpLoads[index] = item;
            index++;
        }
        field.set(obj, marsFileUpLoads);
    }

    /**
     * 给参数赋值
     * @param field 字段
     * @param obj 对象
     * @param valList 数据
     * @throws Exception 异常
     */
    private static void putAttr(Field field, Object obj, List<String> valList) throws Exception{
        String fieldTypeName = field.getType().getSimpleName().toUpperCase();
        String valStr = valList.get(0);
        if(valStr == null || valStr.equals("")){
            return;
        }
        switch (fieldTypeName){
            case DataType.INT:
            case DataType.INTEGER:
                field.set(obj,Integer.parseInt(valStr));
                break;
            case DataType.BYTE:
                field.set(obj,Byte.parseByte(valStr));
                break;
            case DataType.STRING:
                field.set(obj,valStr);
                break;
            case DataType.CHAR:
            case DataType.CHARACTER:
                field.set(obj,valStr.charAt(0));
                break;
            case DataType.DOUBLE:
                field.set(obj,Double.parseDouble(valStr));
                break;
            case DataType.FLOAT:
                field.set(obj,Float.parseFloat(valStr));
                break;
            case DataType.LONG:
                field.set(obj,Long.parseLong(valStr));
                break;
            case DataType.SHORT:
                field.set(obj,Short.valueOf(valStr));
                break;
            case DataType.BOOLEAN:
                field.set(obj,Boolean.parseBoolean(valStr));
                break;
            case DataType.BIGDECIMAL:
                field.set(obj,new BigDecimal(valStr));
                break;
            case DataType.DATE:
                String fmt = "yyyy-MM-dd HH:mm:ss";
                JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
                if(jsonFormat != null && jsonFormat.pattern() != null && !jsonFormat.pattern().equals("")){
                    fmt = jsonFormat.pattern();
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fmt);
                field.set(obj,simpleDateFormat.parse(valStr));
                break;
            default:
                String[] paramArray = new String[valList.size()];
                paramArray = valList.toArray(paramArray);
                if (field.getType().equals(String[].class)){
                    field.set(obj,paramArray);
                }
                break;
        }
    }
}
