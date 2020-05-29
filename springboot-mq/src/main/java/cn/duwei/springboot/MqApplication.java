package cn.duwei.springboot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * mq相关介绍
 *  RabbitMQ 是一个由 Erlang 语言开发的 AMQP（高级消息队列协议） 的开源实现。
 *      具体特点包括：
 *          可靠性、灵活的路由
 *
 *      消息消费
 *          应答模式（自动、手动）
 *
 */
@SpringBootApplication
public class MqApplication {

    public static void main(String[] args) {

        SpringApplication.run(MqApplication.class);
    }
}
