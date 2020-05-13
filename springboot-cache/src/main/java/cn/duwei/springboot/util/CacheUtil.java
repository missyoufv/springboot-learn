package cn.duwei.springboot.util;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;

@Slf4j
public class CacheUtil {

    @Autowired
    private CacheManager cacheManager;


    /**
     * 根据cacheName获取对应的缓存
     *
     * @param cacheName
     * @return
     */
    public CaffeineCache getCacheByName(String cacheName) {

        return (CaffeineCache) cacheManager.getCache(cacheName);
    }

    /**
     * 根据ekey 和cachename 获取数据
     *
     * @param key
     * @param cacheName
     * @param typeReference
     * @param <T>
     * @return
     */
    public <T> T getValue(Object key, String cacheName, TypeReference<T> typeReference) {
        CaffeineCache cache = getCacheByName(cacheName);
        if (null == cache) {
            log.info("get cache error , cache is null");
            return null;
        }
        Cache.ValueWrapper valueWrapper = cache.get(key);
        if (valueWrapper == null) {
            return null;
        }
        Object value = valueWrapper.get();
        if (value == null) {
            return null;
        }
        return JsonUtil.toBean(value.toString(), typeReference);
    }

    /**
     * 从缓存获取数据
     *
     * @param key
     * @param cacheName
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getValue(Object key, String cacheName, Class<T> clazz) {
        CaffeineCache caffeineCache = getCacheByName(cacheName);
        if (null == caffeineCache) {
            log.info("get cache error, cache is null, cacheName:{}", cacheName);
            return null;
        }

        CaffeineCache.ValueWrapper valueWrapper = caffeineCache.get(key);
        if (null == valueWrapper) {
            return null;
        }
        Object value = valueWrapper.get();
        if (null == value) {
            return null;
        }
        return JsonUtil.toBean((String) value, clazz);
    }

    /**
     * 将数据设置到本地缓存中
     *
     * @param key
     * @param value
     * @param cacheName
     */
    public void setValue(Object key, Object value, String cacheName) {
        CaffeineCache cache = getCacheByName(cacheName);
        if (null == cache) {
            log.info("set cache error, cache is null, cacheName:{}", cacheName);
            return;
        }
        cache.put(key, JsonUtil.toJson(value));
    }

    /**
     * 删除指定的key
     *
     * @param key
     * @param cacheName
     */
    public void removeKey(Object key, String cacheName) {
        CaffeineCache cache = getCacheByName(cacheName);
        if (null == cache) {
            log.info("remove key error, cache is null, cacheName:{}", cacheName);
            return;
        }
        cache.evict(key);
    }
}
