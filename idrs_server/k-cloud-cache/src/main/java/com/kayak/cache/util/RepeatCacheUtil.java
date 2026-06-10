package com.kayak.cache.util;

import com.kayak.cache.RepeatCache;
import com.kayak.core.system.SysBeans;

public class RepeatCacheUtil {
    private static RepeatCache repeatCache;
    public static boolean checkRequestRepeat(String key) {
        if(null==repeatCache){
            repeatCache= SysBeans.getBean("repeatCache");
           return repeatCache.checkRequestRepeat(key);
        }
        return repeatCache.checkRequestRepeat(key);
    }
}
