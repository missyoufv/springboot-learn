package cn.duwei.springboot.controller.delayQueue;

import cn.duwei.springboot.service.delayQueue.DelayQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 延时队列就是一种带有延迟功能的消息队列,有一下几种实现及优缺点
 */
@Slf4j
@RestController
@RequestMapping("/queue")
public class DelayQueueController {

    @Autowired
    private DelayQueueService delayQueueService;

    /**
     * Java中java.util.concurrent.DelayQueue
     *  优点：JDK自身实现，使用方便，量小适用
     *  缺点：队列消息处于jvm内存，不支持分布式运行和消息持久化
     *
     * @return
     */
    @GetMapping("/delayQueue/show")
    public String delayQueueShow() {
        delayQueueService.invoke_delayQueue_queue();
        return "SUCCESS";
    }

    /**
     * 通过redis实现延时队列
     * @return
     */
    @GetMapping("/redis/show")
    public String redisShow() {
        delayQueueService.invoke_delayQueue_redis();
        return "SUCCESS";
    }
}
