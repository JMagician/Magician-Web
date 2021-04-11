package com.magician.web;

public class MagicianWebConfig {

    private static String scanPath;

    public static String getScanPath() {
        return scanPath;
    }

    public static void setScanPath(String scanPath) {
        MagicianWebConfig.scanPath = scanPath;
    }
}
