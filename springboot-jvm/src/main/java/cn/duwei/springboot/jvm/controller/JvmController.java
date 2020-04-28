package cn.duwei.springboot.jvm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 模拟线上问题，了解性能分析问题定位流程
 */

@RestController
@Slf4j
public class JvmController {


    /**
     * 死循环，模拟cup占用满场景
     * @return
     */
    @GetMapping(value = "/cpu/loop")
    public void loop() {
        log.info("invoke the the method loop");
        Thread.currentThread().setName("loop-thread");
        int num = 0;
        List<Integer> list = new ArrayList<>();
        while (true) {
            num++;
            if (num == Integer.MAX_VALUE) {
                log.info(" num is the max value :{}", num);
                num = 0;
            }
            list.add(num);
        }
    }


    @GetMapping(value ="/memory/leak")
    public String memoryLeak() {
        log.info("invoke the method method memoryLeak");
        for (int i = 0; i < 600;i++) {
            ThreadLocal<byte[]> threadLocal = new ThreadLocal<>();
            threadLocal.set(new byte[1024*1024]);
        }

        return "SUCCESS";
    }

    /**
     * 查看cpu占用满了的情况，是否对普通接口有影响
     * @return
     */
    @GetMapping(value = "/health/check")
    public String healthCheck() throws Exception{
        log.info("invoke the head check api");

        Callable<Integer> callable = () -> {
            int sum = 0;
            for (int i = 0; i < 1000; i++) {
                sum = sum + i;
            }
            return sum;
        };
        FutureTask<Integer> task = new FutureTask<>(callable);
        Thread thread = new Thread(task);
        thread.start();
        thread.join();
        log.info("the result is :{}",task.get());
        return "SUCCESS : " + task.get();
    }
}
