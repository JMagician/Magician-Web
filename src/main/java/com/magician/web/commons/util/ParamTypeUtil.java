package com.magician.web.commons.util;

/**
 * Content-type of the http request
 */
public class ParamTypeUtil {

    /**
     * regular form submission
     */
    public static final String URL_ENCODED = "application/x-www-form-urlencoded";

    /**
     * json submit
     */
    public static final String JSON = "application/json";

    /**
     * formData submit
     */
    public static final String FORM_DATA = "multipart/form-data";

    /**
     * Is it in json format
     * @param contentType
     * @return
     */
    public static boolean isJSON(String contentType){
        if(contentType == null){
            return false;
        }
        contentType = contentType.toLowerCase();
        return contentType.startsWith(JSON) || contentType.equals(JSON);
    }

    /**
     * Is it in formData format
     * @param contentType
     * @return
     */
    public static boolean isFormData(String contentType){
        if(contentType == null){
            return false;
        }
        contentType = contentType.toLowerCase();
        return contentType.startsWith(FORM_DATA) || contentType.equals(FORM_DATA);
    }

    /**
     * Is it in form format
     * @param contentType 内容类型
     * @return
     */
    public static boolean isUrlEncoded(String contentType){
        if(contentType == null){
            return false;
        }
        contentType = contentType.toLowerCase();
        return contentType.startsWith(URL_ENCODED) || contentType.equals(URL_ENCODED);
    }
}
