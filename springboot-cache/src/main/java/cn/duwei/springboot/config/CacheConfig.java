package cn.duwei.springboot.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * Caffeine配置
 * initialCapacity=[integer]: 初始的缓存空间大小
 * maximumSize=[long]: 缓存的最大条数
 * maximumWeight=[long]: 缓存的最大权重
 * expireAfterAccess=[duration]: 最后一次写入或访问后经过固定时间过期
 * expireAfterWrite=[duration]: 最后一次写入后经过固定时间过期
 * refreshAfterWrite=[duration]: 创建缓存或者最近一次更新缓存后经过固定的时间间隔，刷新缓存
 * weakKeys: 打开key的弱引用
 * weakValues：打开value的弱引用
 * softValues：打开value的软引用
 * recordStats：开发统计功能
 *
 * 注意：
 * expireAfterWrite和expireAfterAccess同事存在时，以expireAfterWrite为准。
 * maximumSize和maximumWeight不可以同时使用
 * weakValues和softValues不可以同时使用
 */
@ConfigurationProperties(prefix = "memory.cache")
@Component
@Data
public class CacheConfig {

    private List<CacheInfo> cacheInfoList = new ArrayList<>();


    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        //把各个cache注册到cacheManager中
        List<CaffeineCache> caches = new ArrayList();
        cacheInfoList.forEach(cacheInfo -> caches.add(new CaffeineCache(cacheInfo.getName(),
                Caffeine.newBuilder()
                    .expireAfterWrite(cacheInfo.getTtl(),TimeUnit.SECONDS)
                    .maximumSize(cacheInfo.getMaxSize())
                    .initialCapacity(cacheInfo.getInitSize())
                    .build())));
        cacheManager.setCaches(caches);
        return cacheManager;
    }
}
