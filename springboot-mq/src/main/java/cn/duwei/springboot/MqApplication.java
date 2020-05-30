package cn.duwei.springboot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * mq相关介绍
 *  RabbitMQ 是一个由 Erlang 语言开发的 AMQP（高级消息队列协议） 的开源实现。
 *      特性包括：
 *          可靠性、灵活的路由、消息集群、可用性、多种协议、多语言客户端、管理界面、插件机制
 *      交换机类型：
 *          直连交换机（direct)、主题交换机（topic)、广播交换机（fanout)
 *
 *
 *      进阶：
 *          1、自动删除没有人消费的消息？（防止消息堆积、占用空间）
 *              设置队列消息的ttl
 *              设置消息的ttl
 *          2、死信队列 Dead Letter Queue 死信交换机 DLX
 *
 *             哪些情况消息会变成死信
 *                  1、消息过期
 *                  2、手动应答、拒绝消息 reject nack 且 requeue 为false
 *                  3、队列达到最大长度、先入队消息会被删除
 *          3、消息优先得到消费
 *              设置消息队列、和消息优先级 proirity
 *
 *          4、实现消息延迟发送
 *              1、延迟发送插件
 *              2、设置消息过期时间，然后用死信队列
 *
 *
 *         5、可靠性投递
 *              服务端确认：
 *                  transation 模式 同步 效率低
 *                  confirm 模式 异步确认
 *              交换机、队列、消息 持久化
 *
 *         6、消费者回调、补偿机制、消息幂等性、消息顺序性
 *
 *
 *         7、高可用 集群、镜像队列
 *
 *
 */
@SpringBootApplication
public class MqApplication {

    public static void main(String[] args) {

        SpringApplication.run(MqApplication.class);
    }
}
