package com.kayak.pms.opFlow.engine.cache;

/**
 * Created by daniel on 17/03/2017.
 */
public interface CacheManager {
    /**
     * 根据cache的名称获取cache。如果不存在，默认新建并返回
     *
     * @param name
     * @return Cache
     * @throws CacheException
     */
    <K, V> Cache<K, V> getCache(String name) throws CacheException;

    /**
     * 销毁cache
     *
     * @throws CacheException
     */
    void destroy() throws CacheException;
}
