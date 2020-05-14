package cn.duwei.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 缓存相关功能模块，功能点如下：
 *      1、springboot 整合caffeine
 *
 *
 * Spring缓存支持
 * Spring定义了org.springframework.cache.CacheManager 和 org.springframework.cache.Cache 接口来统一不同缓存技术。 其中CacheManager是Spring提供的各种缓存技术抽象接口，
 * 内部使用Cache接口进行缓存的增删改查操作，我们一般不会直接和Cache打交道。
 *
 */
@SpringBootApplication
@EnableCaching
public class CacheApplication {

    public static void main(String[] args) {

        SpringApplication.run(CacheApplication.class);
    }
}
