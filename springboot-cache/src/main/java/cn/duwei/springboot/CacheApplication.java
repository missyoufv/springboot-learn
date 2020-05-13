package cn.duwei.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 缓存相关功能模块，功能点如下：
 *      1、springboot 整合caffeine
 */
@SpringBootApplication
@EnableCaching
public class CacheApplication {

    public static void main(String[] args) {

        SpringApplication.run(CacheApplication.class);
    }
}
