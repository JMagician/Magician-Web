package com.magician.web.cloud.load.cache;

/**
 * 接口实体
 */
public class LocalCacheRouteModel {

    /**
     * 所在的服务名称
     */
    private String serverName;

    /**
     * 所在的服务地址
     */
    private String serverUrl;

    /**
     * Route注解内设置的 path属性值
     */
    private String path;

    /**
     * 接口全路径，http://xxx/xxx/xx
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 缓存创建时间
     */
    private long createTime = System.currentTimeMillis();

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method.toUpperCase();
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
