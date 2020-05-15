package cn.duwei.springboot.service.delayQueue;


import cn.duwei.springboot.entity.CacheItem;
import cn.duwei.springboot.entity.Order;
import cn.duwei.springboot.util.JsonUtil;
import cn.duwei.springboot.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 实现延迟队列的几种方式
 * 1、delayQueue
 * 2、Quartz 定时任务
 * 3、Redis sorted set
 * 4、Redis 过期回调
 * 5、RabbitMQ 延时队列
 */
@Service
@Slf4j
public class DelayQueueService {

    @Autowired
    private RedisUtil redisUtil;

    private static final String DELAY_QUEUE_PREFIX = "delay:queue:prefix";

    /**
     * jdk提供DelayQueues实现延时队列 延时消费消息
     */
    public void invoke_delayQueue_queue() {

        Order order1 = new Order("202005132141", 5, TimeUnit.SECONDS);
        Order order2 = new Order("202005132142", 10, TimeUnit.SECONDS);

        java.util.concurrent.DelayQueue<Order> delayQueue = new java.util.concurrent.DelayQueue<>();
        delayQueue.put(order1);
        delayQueue.put(order2);
        new Thread(() -> {
            try {
                for (; delayQueue.size() != 0; ) {
                    Order order = delayQueue.poll();
                    if (order == null) {
                        System.out.println("没有超时订单，请稍后再来");
                    } else {
                        System.out.println("订单号为:"+order.getOrderNum()+"的订单超时，请关闭订单，释放库存");
                    }
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }).start();
    }

    /**
     * 基于redis 的zset数据结构实现
     */
    public void invoke_delayQueue_redis() {

        try {
            /**
             * 生产订单，放入延时队列
             */
            new Thread(() -> {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                for (int i = 0; i < 10; i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.SECOND,2*i);
                    String name = "过期时间："+ sdf.format(calendar.getTime());
                    CacheItem cacheItem = new CacheItem("202005140"+i, name);
                    redisUtil.zsetAdd(DELAY_QUEUE_PREFIX, JsonUtil.toJson(cacheItem), calendar.getTime().getTime());
                }
            }).start();

            Thread.sleep(1);
            while (true) {
                // 消费redis队列消息
                long size = redisUtil.zCount(DELAY_QUEUE_PREFIX);
                if (size <= 0) {
                    System.out.println("redis消息队列消息以消费完");
                    break;
                }
                Set<String> sets = redisUtil.zRangeByScore(DELAY_QUEUE_PREFIX, 0, System.currentTimeMillis(), 0, 1);
                if (!CollectionUtils.isEmpty(sets)) {
                    String content = sets.iterator().next();
                    redisUtil.zRem(DELAY_QUEUE_PREFIX,content);
                    CacheItem cacheItem = JsonUtil.toBean(content, CacheItem.class);
                    System.out.println("订单号为:"+ cacheItem.getId() +"的订单过期，" +cacheItem.getName());
                } else {
                    Thread.sleep(500);
                }
            }




        } catch (Exception ex) {

            log.error("error is :{}",ex);
        }

    }
}
