package com.magician.web.cloud.config;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * 微服务配置
 */
public class CloudConfig {

    /**
     * 服务名称
     */
    private static String serverName;

    /**
     * 服务的url
     * 别的节点调用本节点的接口，需要以这个为前缀
     */
    private static String serverUrl;

    /**
     * 要连接的节点
     * 实现接口缓存数据 交换的
     */
    private static String connection;

    /**
     * 代理
     */
    private static InetSocketAddress proxy;

    /**
     * 超时时间
     */
    private static long timeout = 5000;

    /**
     * 秘钥，给客户端查询微服务的接口列表用的
     */
    private static String secretKey = "7a98121c-11d1-45f5-8c65-2fecb3948799";

    public static String getServerName() {
        return serverName;
    }

    public static void setServerName(String serverName) {
        CloudConfig.serverName = serverName;
    }

    public static String getServerUrl() {
        return serverUrl;
    }

    public static void setServerUrl(String serverUrl) {
        CloudConfig.serverUrl = serverUrl;
    }

    public static String getConnection() {
        return connection;
    }

    public static void setConnection(String connection) {
        CloudConfig.connection = connection;
    }

    public static long getTimeout() {
        return timeout;
    }

    public static void setTimeout(long timeout) {
        CloudConfig.timeout = timeout;
    }

    public static InetSocketAddress getProxy() {
        return proxy;
    }

    public static void setProxy(InetSocketAddress proxy) {
        CloudConfig.proxy = proxy;
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public static void setSecretKey(String secretKey) {
        CloudConfig.secretKey = secretKey;
    }
}
