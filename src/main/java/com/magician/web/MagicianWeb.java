package com.magician.web;

import com.magician.web.cloud.config.CloudConfig;
import com.magician.web.cloud.load.CloudLoad;
import com.magician.web.cloud.load.task.CommunicationTask;
import com.magician.web.commons.util.MsgUtil;
import com.magician.web.commons.util.StringUtil;
import com.magician.web.mvc.execute.MvcExecute;
import com.magician.web.mvc.load.MvcLoad;
import io.magician.application.request.MagicianRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * MagicianWeb
 */
public class MagicianWeb {

    private static Logger logger = LoggerFactory.getLogger(MagicianWeb.class);

    /**
     * 创建一个web
     * @return
     */
    public static MagicianWeb createWeb() throws Exception {
        /* 加载资源 */
        MvcLoad.load();

        return new MagicianWeb();
    }

    /**
     * 连接哪个节点
     * 实现接口缓存数据 交换的
     * @param url
     */
    public MagicianWeb connection(String url){
        CloudConfig.setConnection(url);
        return this;
    }

    /**
     * 服务名称
     * @param name
     */
    public MagicianWeb serverName(String name){
        CloudConfig.setServerName(name);
        return this;
    }

    /**
     * 服务URL
     * 别的节点调用本节点的接口，需要以这个为前缀
     * @param url
     */
    public MagicianWeb serverUrl(String url){
        CloudConfig.setServerUrl(url);
        return this;
    }

    /**
     * 设置请求微服务节点的超时时间
     * @param timeout
     * @return
     */
    public MagicianWeb timeout(long timeout){
        CloudConfig.setTimeout(timeout);
        return this;
    }

    /**
     * 添加代理
     * @param inetSocketAddress
     * @return
     */
    public MagicianWeb proxy(InetSocketAddress inetSocketAddress){
        CloudConfig.setProxy(inetSocketAddress);
        return this;
    }

    /**
     * 配置设置完成
     * Magician服务启动前 调用
     */
    public void end() throws Exception {
        if(StringUtil.isNull(CloudConfig.getServerName())){
            throw new Exception("ServerName 不可以为空");
        }
        if(StringUtil.isNull(CloudConfig.getServerUrl())){
            throw new Exception("ServerUrl 不可以为空");
        }
        if(StringUtil.isNull(CloudConfig.getConnection())){
            throw new Exception("Connection 不可以为空");
        }
        if(StringUtil.isNull(CloudConfig.getSecretKey())){
            throw new Exception("SecretKey 不可以为空");
        }
        if(CloudConfig.getTimeout() < 1){
            throw new Exception("Timeout 必须大于0");
        }

        CloudLoad.initOwnerRouteInsertToLocalCacheRouteMap();
        CloudLoad.communication();
        CommunicationTask.run();
    }

    /**
     * 执行请求
     * 给Magician的核心handler调用的
     */
    public static void request(MagicianRequest request) throws Exception {
        try {
            /* 执行业务逻辑 */
            MvcExecute.execute(request);
        } catch (Exception e){
            logger.error("执行MagicianWeb出现异常", e);

            request.getResponse()
                    .sendJson(MsgUtil.getMsg(500, e.getMessage()));
        }
    }
}
