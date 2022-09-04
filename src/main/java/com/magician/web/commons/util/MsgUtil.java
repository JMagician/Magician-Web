package com.magician.web.commons.util;

import java.util.HashMap;
import java.util.Map;

/**
 * error message tool
 */
public class MsgUtil {

    /**
     * Get error message
     * @param errorCode code
     * @param errorMsg 信息
     * @return 异常
     */
    public static String getMsg(Integer errorCode, String errorMsg){
        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("error_code", errorCode);
        jsonObject.put("error_info", errorMsg);
        return JSONUtil.toJSONString(jsonObject);
    }
}
