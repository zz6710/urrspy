package com.kayak.pms.opFlow.engine.cache.memory;

import com.kayak.pms.opFlow.engine.cache.Cache;
import com.kayak.pms.opFlow.engine.cache.CacheException;
import com.kayak.pms.opFlow.engine.helper.AssertHelper;

import java.util.Map;

/**
 * Created by daniel on 17/03/2017.
 */
public class MemoryCache<K, V>  implements Cache<K, V> {

    private final Map<K, V> map;
    public MemoryCache(Map<K,V> backingMap) {
        AssertHelper.notNull(backingMap);
        this.map = backingMap;
    }

    @Override
    public V get(K key) throws CacheException {
        return this.map.get(key);
    }

    @Override
    public V put(K key, V value) throws CacheException {
        return this.map.put(key, value);
    }

    @Override
    public V remove(K key) throws CacheException {
        return this.map.remove(key);
    }

    @Override
    public void clear() throws CacheException {
        this.map.clear();
    }
}
