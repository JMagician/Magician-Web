package com.magician.web;

import com.magician.web.core.constant.MagicianWebConstant;
import com.magician.web.core.util.MesUtil;
import com.magician.web.execute.ApiExecute;
import com.magician.web.load.ApiLoad;
import io.magician.tcp.http.request.MagicianRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * 扫描本项目的web接口
     * @param packageName
     * @return
     */
    public MagicianWeb scan(String packageName){
        MagicianWebConfig.setScanPath(packageName);
        return this;
    }

    /**
     * 执行请求
     */
    public void request(MagicianRequest request){
        try {
            /* 加载资源 */
            ApiLoad.load();

            /* 执行业务逻辑 */
            ApiExecute.execute(request);
        } catch (Exception e){
            logger.error("执行MagicianWeb出现异常", e);

            request.getResponse()
                    .sendJson(200, MesUtil.getMes(500, e.getMessage()));
        }
    }
}
