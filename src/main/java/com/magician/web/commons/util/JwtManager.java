package com.magician.web.commons.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.magician.web.commons.constant.DataType;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT管理类
 */
public class JwtManager {
    /**
     * token秘钥
     */
    private String secret = "b18af1cf-563a-4394-ac98-0b31013c7ba5";
    /**
     * token 过期时间的单位
     */
    private int calendarField = Calendar.MILLISECOND;
    /**
     * token 过期时间
     */
    private int calendarInterval = 86400;

    public JwtManager setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public JwtManager setCalendarField(int calendarField) {
        this.calendarField = calendarField;
        return this;
    }

    public JwtManager setCalendarInterval(int calendarInterval) {
        this.calendarInterval = calendarInterval;
        return this;
    }

    /**
     * 创建一个JWT管理类
     * @return
     */
    public static JwtManager builder() {
        JwtManager jwtManager = new JwtManager();
        return jwtManager;
    }

    /**
     * WT生成Token.
     * @param obj
     * @return str
     */
    public String createToken(Object obj) throws Exception {
        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(calendarField, calendarInterval);
        Date expiresDate = nowTime.getTime();

        // header Map
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        JWTCreator.Builder builder = JWT.create().withHeader(map);

        builder = getBuilder(builder,obj);

        builder.withIssuedAt(iatDate); // sign time
        builder.withExpiresAt(expiresDate); // expire time
        String token = builder.sign(Algorithm.HMAC256(secret)); // signature

        return token;
    }

    /**
     * 解析对象，并存入JWT
     * @param builder
     * @param obj
     * @return
     * @throws Exception
     */
    private JWTCreator.Builder getBuilder(JWTCreator.Builder builder, Object obj) throws Exception {
        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value =  field.get(obj);
            if(value == null){
                continue;
            }
            builder.withClaim(field.getName(), value.toString());
        }
        return builder;
    }

    /**
     * 校验Token
     *
     * @param token
     * @return map
     */
    public boolean verifyToken(String token) {
        Map<String, Claim> claims = decryptToken(token);
        return claims != null;
    }

    /**
     * 解密Token
     *
     * @param token
     * @return map
     */
    private Map<String, Claim> decryptToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            jwt = verifier.verify(token);
            return jwt.getClaims();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据Token获取存进去的对象
     * @param token
     * @param cls
     * @param <T>
     * @return obj
     */
    public <T> T  getObject(String token,Class<T> cls) throws Exception {
        Object obj = cls.getDeclaredConstructor().newInstance();
        try {
            Map<String, Claim> claims = decryptToken(token);
            if(claims == null || claims.isEmpty()){
                return null;
            }
            for (String key : claims.keySet()) {
                Field field = getField(key, cls);
                if(field == null){
                    continue;
                }
                field.setAccessible(true);
                Claim value = claims.get(key);
                if(value == null){
                    continue;
                }
                setField(obj, field, value);
            }
            return (T)obj;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 给字段赋值
     * @param obj
     * @param field
     * @param value
     * @throws Exception
     */
    private void setField(Object obj, Field field, Claim value) throws Exception {
        String type = field.getType().getSimpleName().toUpperCase();
        String val = value.asString();
        switch (type){
            case DataType.INT:
            case DataType.INTEGER:
                field.set(obj,Integer.parseInt(val));
                return;
            case DataType.BYTE:
                field.set(obj,Byte.parseByte(val));
                return;
            case DataType.STRING:
                field.set(obj,val);
                return;
            case DataType.CHAR:
            case DataType.CHARACTER:
                field.set(obj,val.charAt(0));
                return;
            case DataType.DOUBLE:
                field.set(obj,Double.parseDouble(val));
                return;
            case DataType.FLOAT:
                field.set(obj,Float.parseFloat(val));
                return;
            case DataType.LONG:
                field.set(obj,Long.parseLong(val));
                return;
            case DataType.SHORT:
                field.set(obj,Short.parseShort(val));
                return;
            case DataType.BOOLEAN:
                field.set(obj,Boolean.parseBoolean(val));
                return;
        }
    }

    /**
     * 获取字段
     * @param name
     * @param cls
     * @return
     */
    private Field getField(String name, Class cls){
        try {
            return cls.getDeclaredField(name);
        } catch (Exception e){
            return null;
        }
    }
}
