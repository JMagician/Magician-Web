package com.magician.web.cloud.config;

/**
 * 微服务配置
 */
public class CloudConfig {

    /**
     * 服务名称
     */
    private String serverName;

    /**
     * 服务的url
     * 别的节点调用本节点的接口，需要以这个为前缀
     */
    private String serverUrl;

    /**
     * 要连接的节点
     * 实现接口缓存数据 交换的
     */
    private String connection;

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

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }
}
