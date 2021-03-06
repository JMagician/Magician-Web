package com.magician.web.commons.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 错误提示信息 工具类
 */
public class MsgUtil {

    /**
     * 获取错误提示信息
     * @param errorCode code
     * @param errorMsg 信息
     * @return 异常
     */
    public static String getMsg(Integer errorCode, String errorMsg){
        try {
            Map<String, Object> jsonObject = new HashMap<>();
            jsonObject.put("error_code", errorCode);
            jsonObject.put("error_info", errorMsg);
            return JSONUtil.toJSONString(jsonObject);
        } catch (Exception e){
            return "error";
        }
    }
}
