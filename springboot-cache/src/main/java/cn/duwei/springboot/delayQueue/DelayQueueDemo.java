package cn.duwei.springboot.delayQueue;


import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * 实现延迟队列的几种方式
 * 1、delayQueue
 * 2、Quartz 定时任务
 * 3、Redis sorted set
 * 4、Redis 过期回调
 * 5、RabbitMQ 延时队列
 */
public class DelayQueueDemo {

    public static void main(String[] args) {

        invoke_delayQueue_method();
    }

    /**
     * jdk提供的延时队列
     */
    private static void invoke_delayQueue_method() {

        Order order1 = new Order("202005132141", 5, TimeUnit.SECONDS);
        Order order2 = new Order("202005132142", 10, TimeUnit.SECONDS);

        DelayQueue<Order> delayQueue = new DelayQueue<>();
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
}
