package com.magician.web.commons.util;

import com.magician.web.mvc.core.annotation.Verification;
import com.magician.web.commons.constant.DataType;
import io.magician.application.request.MagicianRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验前端传参
 */
public class ParamsCheckUtil {

    private static Logger logger = LoggerFactory.getLogger(ParamsCheckUtil.class);

    /**
     * 校验参数
     * @param params 参数集合
     * @param url 路径
     * @return 校验结果
     */
    public static String checkParam(Object[] params, String url){
        if(params == null){
            return null;
        }
        Class requestClass = MagicianRequest.class;

        for(Object obj : params){
            if(obj == null){
                continue;
            }
            Class cls = obj.getClass();
            if(requestClass.equals(cls)){
                continue;
            }
            String result = checkParam(cls, obj, url);
            if(result != null){
                return result;
            }
        }
        return null;
    }

    /**
     * 校验参数
     * @param cls 参数类型
     * @param obj 参数对象
     * @return 校验结果
     */
    private static String checkParam(Class<?> cls, Object obj, String url) {
        try {
            Field[] fields = cls.getDeclaredFields();
            for(Field field : fields){
                field.setAccessible(true);
                /* 获取校验的注解 */
                Verification verification = field.getAnnotation(Verification.class);
                if(verification == null){
                    continue;
                }

                /* 判断此注解是否生效与当前api，如果不生效那就直接跳入下一次循环 */
                String[] apis = verification.apis();
                if(!isThisApi(apis, url)){
                    continue;
                }

                /* 校验参数是否符合规则 */
                Object val = field.get(obj);
                int errorCode = 1128;

                String fieldTypeName = field.getType().getSimpleName().toUpperCase();

                switch (fieldTypeName){
                    case DataType.INT:
                    case DataType.INTEGER:
                        if(notNull(verification, val) == false){
                            return MsgUtil.getMsg(errorCode, verification.msg());
                        }

                        if(StringUtil.isNull(verification.min()) == false){
                            if(val == null){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                            if(Integer.parseInt(val.toString()) < Integer.parseInt(verification.min())){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                        }
                        if(StringUtil.isNull(verification.max()) == false){
                            if(val == null){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                            if(Integer.parseInt(val.toString()) > Integer.parseInt(verification.max())){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                        }
                        continue;
                    case DataType.LONG:
                        if(notNull(verification, val) == false){
                            return MsgUtil.getMsg(errorCode, verification.msg());
                        }

                        if(StringUtil.isNull(verification.min()) == false){
                            if(val == null){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                            if(Long.parseLong(val.toString()) < Long.parseLong(verification.min())){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                        }
                        if(StringUtil.isNull(verification.max()) == false){
                            if(val == null){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                            if(Long.parseLong(val.toString()) > Long.parseLong(verification.max())){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                        }
                        continue;
                    case DataType.DOUBLE:
                        if(notNull(verification, val) == false){
                            return MsgUtil.getMsg(errorCode, verification.msg());
                        }

                        if(StringUtil.isNull(verification.min()) == false){
                            if(val == null){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                            if(Double.parseDouble(val.toString()) < Double.parseDouble(verification.min())){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                        }
                        if(StringUtil.isNull(verification.max()) == false){
                            if(val == null){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                            if(Double.parseDouble(val.toString()) > Double.parseDouble(verification.max())){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                        }
                        continue;
                    case DataType.FLOAT:
                        if(notNull(verification, val) == false){
                            return MsgUtil.getMsg(errorCode, verification.msg());
                        }

                        if(StringUtil.isNull(verification.min()) == false){
                            if(val == null){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                            if(Float.parseFloat(val.toString()) < Float.parseFloat(verification.min())){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                        }
                        if(StringUtil.isNull(verification.max()) == false){
                            if(val == null){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                            if(Float.parseFloat(val.toString()) > Float.parseFloat(verification.max())){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                        }
                        continue;
                    case DataType.BIGDECIMAL:
                        if(notNull(verification, val) == false){
                            return MsgUtil.getMsg(errorCode, verification.msg());
                        }

                        if(StringUtil.isNull(verification.min()) == false){
                            if(val == null){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                            if(new BigDecimal(val.toString()).compareTo(new BigDecimal(verification.min())) < 0){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                        }
                        if(StringUtil.isNull(verification.max()) == false){
                            if(val == null){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                            if(new BigDecimal(val.toString()).compareTo(new BigDecimal(verification.max())) > 0){
                                return MsgUtil.getMsg(errorCode, verification.msg());
                            }
                        }
                        continue;
                    case DataType.STRING:
                        if(!reg(val, verification.reg())){
                            return MsgUtil.getMsg(errorCode, verification.msg());
                        }
                        if(!notNull(verification, val)){
                            return MsgUtil.getMsg(errorCode, verification.msg());
                        }
                        continue;
                    case DataType.SHORT:
                    case DataType.BOOLEAN:
                    case DataType.DATE:
                    case DataType.BYTE:
                    case DataType.CHAR:
                    case DataType.CHARACTER:
                        if(!notNull(verification, val)){
                            return MsgUtil.getMsg(errorCode, verification.msg());
                        }
                        continue;
                    default:
                        if (field.getType().equals(String[].class) && !notNull(verification, val)){
                            return MsgUtil.getMsg(errorCode, verification.msg());
                        }
                        continue;
                }
            }
            return null;
        } catch (Exception e){
            logger.error("校验参数出现异常",e);
            return null;
        }
    }

    /**
     * 校验正则
     * @param val 数据
     * @param reg 正则
     * @return 结果
     */
    private static boolean reg(Object val,String reg){
        if(StringUtil.isNull(reg)){
            return true;
        }
        if(StringUtil.isNull(val)){
            return false;
        }
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(val.toString());
        return matcher.matches();
    }

    /**
     * 非空校验
     * @param verification 注解
     * @param val 数据
     * @return 结果
     */
    private static boolean notNull(Verification verification, Object val){
        if(verification.notNull() == false){
            return true;
        }
        if(StringUtil.isNull(val)){
            return false;
        }
        return true;
    }

    /**
     * 校验apis列表里是否包含此api
     * @param url 此api
     * @param apis api列表
     * @return
     */
    private static boolean isThisApi(String[] apis, String url){
        if(apis == null || apis.length < 1){
            return true;
        }

        for(String api : apis){
            if(MatchUtil.isMatch(api.toUpperCase(), url.toUpperCase())){
                return true;
            }
        }
        return false;
    }
}
