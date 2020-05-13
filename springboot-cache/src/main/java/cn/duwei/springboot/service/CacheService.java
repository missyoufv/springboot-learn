package cn.duwei.springboot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Cacheable 触发缓存入口（这里一般放在创建和获取的方法上）
 * @CacheEvict 触发缓存的eviction（用于删除的方法上）
 * @CachePut 更新缓存且不影响方法执行（用于修改的方法上，该注解下的方法始终会被执行）
 * @Caching 将多个缓存组合在一个方法上（该注解可以允许一个方法同时设置多个注解）
 * @CacheConfig 在类级别设置一些缓存相关的共同配置（与其它缓存配合使用）
 *
 */
@Service
@Slf4j
public class CacheService {
    @Cacheable(value = "cacheFiveMinute",key = "#key")
    public void getCache(String key) {
        log.info(" get cache test");
    }

    @CacheEvict(value = "cacheFiveMinute",key = "#key")
    public void delete(String key) {
        log.info(" delete cache test");
    }

    @CachePut(value = "cacheFiveMinute",key = "#key")
    public void put(String key) {
        log.info(" put cache test");
    }
}
