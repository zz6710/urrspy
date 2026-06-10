package com.kayak.pms.opFlow.engine.cache.memory;

import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.engine.cache.Cache;
import com.kayak.pms.opFlow.engine.cache.CacheException;
import com.kayak.pms.opFlow.engine.cache.CacheManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by daniel on 17/03/2017.
 */
public class MemoryCacheManager implements CacheManager {
    private final ConcurrentMap<String, Cache> caches;

    public MemoryCacheManager() {
        this.caches = new ConcurrentHashMap<String, Cache>();
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        if(StringHelper.isEmpty(name)){
            throw new IllegalArgumentException("Cache名称不能为空.");
        }
        Cache cache;

        cache = caches.get(name);
        if(cache == null){
            cache = new MemoryCache<Object, Object>(new ConcurrentHashMap<Object, Object>());
            Cache existing = caches.putIfAbsent(name, cache);
            if (existing != null) {
                cache = existing;
            }
        }
        return cache;
    }

    @Override
    public void destroy() throws CacheException {
        while (!caches.isEmpty()) {
            caches.clear();
        }
    }
}
