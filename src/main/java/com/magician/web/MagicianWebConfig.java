package com.magician.web;

import java.util.HashSet;
import java.util.Set;

/**
 * Web配置
 */
public class MagicianWebConfig {

    private static Set<String> scanPath;

    public static Set<String> getScanPath() {
        return scanPath;
    }

    public static void setScanPath(String scanPath) {
        if(MagicianWebConfig.scanPath == null){
            MagicianWebConfig.scanPath = new HashSet<>();
        }
        MagicianWebConfig.scanPath.add(scanPath);
    }
}
