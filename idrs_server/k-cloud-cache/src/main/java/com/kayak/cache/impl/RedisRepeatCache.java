package com.kayak.cache.impl;

import com.kayak.cache.RepeatCache;
import com.kayak.cache.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

@Service("repeatCache")
@ConditionalOnExpression("${memoryreapeatcache.type:1}==0")
public class RedisRepeatCache implements RepeatCache {
    @Autowired
    private RedisUtils redisUtils;
    @Value("${memoryreapeatcache.time:10}")
    private int time;
    @Value("${memoryreapeatcache.enabled:false}")
    private boolean enabled;
    @Override
    public boolean checkRequestRepeat(String key) {
        // 是否开启防重
        if (!enabled) {
            return false;
        }
        if(redisUtils.hasKey(key)){
            return true;
        }else{
            redisUtils.set(key,"0",time);
            return false;
        }
    }
}
