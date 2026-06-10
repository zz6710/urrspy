package com.kayak.cache.impl;

import com.kayak.cache.RepeatCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service("repeatCache")
@ConditionalOnExpression("${memoryreapeatcache.type:1}==1")
public class MemoryRepeatCache implements RepeatCache {
    private static Map<String, Integer> requestCache = new ConcurrentHashMap<String, Integer>();
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
        if (MemoryRepeatCache.requestCache.containsKey(key)){
            return true;
        }else{
            MemoryRepeatCache.requestCache.put(key,time);
            return false;
        }
    }
    @PostConstruct
    public void ds(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Set<String> keySet = MemoryRepeatCache.requestCache.keySet();
                Iterator<String> iter = keySet.iterator();
                while (iter.hasNext()) {
                    String key = iter.next();
                    int time= MemoryRepeatCache.requestCache.get(key)-2;
                    if(time<=0){
                        MemoryRepeatCache.requestCache.remove(key);
                    }else{
                        MemoryRepeatCache.requestCache.put(key,time);
                    }
                }
            }
        };
        Timer timer = new Timer();
        // 定义开始等待时间  --- 等待 2 秒
        // 1000ms = 1s
        long delay = 2000;
        // 定义每次执行的间隔时间
        long intevalPeriod = 2 * 1000;
        // 安排任务在一段时间内运行
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }
}
