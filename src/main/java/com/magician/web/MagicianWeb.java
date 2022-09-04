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
     * execute request
     */
    public static void request(MagicianRequest request) throws Exception {
        try {
            /* load resource */
            MvcLoad.load();

            /* Execute business logic */
            MvcExecute.execute(request);
        } catch (Exception e){
            logger.error("An exception occurred while executing MagicianWeb", e);

            request.getResponse()
                    .sendErrorMsg(500, e.getMessage());
        }
    }
}
