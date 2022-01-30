package com.magician.web.cloud.load.task;

import com.magician.web.cloud.load.CloudLoad;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 通知任务
 */
public class CommunicationTask {

    /**
     * 轮询间隔时长，毫秒
     */
    private static long loop = 5000;

    /**
     * 定时轮询，跟其他节点的连接，以及接口列表数据交互
     */
    public static void run() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                CloudLoad.communication();
            }
        };

        new Timer().scheduleAtFixedRate(timerTask, System.currentTimeMillis() + loop, loop);
    }
}
