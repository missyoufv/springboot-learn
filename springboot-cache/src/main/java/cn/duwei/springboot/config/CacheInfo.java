package cn.duwei.springboot.config;

import lombok.Data;

@Data
public class CacheInfo {

    /** 默认缓存大小 **/
    private static final int DEFAULT_MAXSIZE = 1000;
    /**
     * 默认初始大小
     */
    private static final int DEFAULT_INIT_SIZE = 100;

    /** 默认缓存过期时间为 120秒 */
    private static final long DEFAULT_TIME_OUT = 120;

    /**
     * 缓存名称
     */
    private String name;

    /**
     * 最大缓存的数量
     */
    private int maxSize = DEFAULT_MAXSIZE;

    /** 初始化的数量 **/
    private int initSize = DEFAULT_INIT_SIZE;
    /**
     * 过期时间
     */
    private long ttl = DEFAULT_TIME_OUT;
}
