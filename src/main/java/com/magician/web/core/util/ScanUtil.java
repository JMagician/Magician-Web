package com.magician.web.core.util;

import com.magician.web.core.cache.MagicianCacheManager;

import java.util.HashSet;
import java.util.Set;

/**
 * 扫描工具类
 */
public class ScanUtil {

    /**
     * 扫描框架的类
     * @param packageName 要扫描的包名
     * @return 扫描出来的包
     * @throws Exception 异常
     */
    public static Set<String> scanClassList(Set<String> packageName) throws Exception {
        Set<String> scanClassList = new HashSet<>();
        for(String pkName : packageName){
            Set<String> classList = ReadClassUtil.loadClassList(pkName);
            scanClassList.addAll(classList);
        }
        MagicianCacheManager.saveScanClassList(scanClassList);
        return scanClassList;
    }
}
