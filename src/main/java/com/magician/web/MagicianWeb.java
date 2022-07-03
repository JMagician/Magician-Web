package com.magician.web;

import com.magician.web.commons.util.MsgUtil;
import com.magician.web.mvc.execute.MvcExecute;
import com.magician.web.mvc.load.MvcLoad;
import io.magician.application.request.MagicianRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MagicianWeb
 */
public class MagicianWeb {

    private static Logger logger = LoggerFactory.getLogger(MagicianWeb.class);

    /**
     * 执行请求
     * 给Magician的核心handler调用的
     */
    public static void request(MagicianRequest request) throws Exception {
        try {
            /* 加载资源 */
            MvcLoad.load();

            /* 执行业务逻辑 */
            MvcExecute.execute(request);
        } catch (Exception e){
            logger.error("执行MagicianWeb出现异常", e);

            request.getResponse()
                    .sendJson(MsgUtil.getMsg(500, e.getMessage()));
        }
    }
}
