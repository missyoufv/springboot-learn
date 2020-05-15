package cn.duwei.springboot.entity;

import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Data
public class Order implements Delayed {

    /**
     * 默认超时时间1分众
     */
    private static final long DEFAULT_TIME_OUT = 60 * 1000;

    /**
     * 单号
     */
    private String orderNum;

    /**
     * 延迟时间 默认是秒
     */
    private long time;


    public Order(String orderNum, long time, TimeUnit timeUnit) {
        this.orderNum = orderNum;
        this.time = System.currentTimeMillis() + timeUnit.toMillis(time);
    }

    public Order(String orderNum) {
        this.orderNum = orderNum;
        this.time = System.currentTimeMillis() + DEFAULT_TIME_OUT;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return this.time - System.currentTimeMillis()  ;
    }

    @Override
    public int compareTo(Delayed o) {

        if (this.getDelay(TimeUnit.SECONDS) - o.getDelay(TimeUnit.SECONDS) > 0) {
            return 1;
        }
        return -1;

    }
}
