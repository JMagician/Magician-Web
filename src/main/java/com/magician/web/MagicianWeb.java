package com.magician.web;

import com.magician.web.cloud.config.CloudConfig;
import com.magician.web.cloud.load.CloudLoad;
import com.magician.web.commons.util.MsgUtil;
import com.magician.web.mvc.execute.ApiExecute;
import com.magician.web.mvc.load.ApiLoad;
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
    public static MagicianWeb createWeb(){
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
    public void end(){
        CloudLoad.communication();
    }

    /**
     * 执行请求
     * 给Magician的核心handler调用的
     */
    public static void request(MagicianRequest request) throws Exception {
        try {
            /* 加载资源 */
            ApiLoad.load();

            /* 执行业务逻辑 */
            ApiExecute.execute(request);
        } catch (Exception e){
            logger.error("执行MagicianWeb出现异常", e);

            request.getResponse()
                    .sendJson(MsgUtil.getMsg(500, e.getMessage()));
        }
    }
}
