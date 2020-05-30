package cn.duwei.springboot.jdk;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 生产者
 *
 *  声明交换机和队列的属性有 durable 持久化 autoDelete 自动删除 exclusive 排他 队列仅当前连接可见
 */
public class RabbitMqProducter {

    public static void main(String[] args) throws IOException {

//        invoke_push_message();
//        invoke_push_message_toExhange();
//        invoke_push_message_toNoBindQueue();
//        invoke_push_message_toBindQueue();

//        invoke_push_message_setting_ttl();

//        invoke_push_message_dlx();

//        invoke_push_message_prority();

//        invoke_push_message_transaction();

        invoke_push_message_confirm();

    }

    /**
     * 通过confirm确认机制 保证消息投递可靠性
     */
    private static void invoke_push_message_confirm() throws IOException{

        Connection connection = ConnectionFactoryUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("test_confirm_queue", false, false, false, null);


        try {
            channel.confirmSelect();
            channel.basicPublish("", "test_confirm_queue", null, "confirm comit message".getBytes());
//            if (channel.waitForConfirms()) {
//                System.out.println("消息发送成功");
//            }

            // 异步监听
            channel.addConfirmListener(new ConfirmListener() {
                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("消息发送成功");
                }

                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("消息发送失败");
                }
            });

        } catch (Exception ex) {
            channel.txRollback();
            System.out.println("消息发送失败");
        }
        ConnectionFactoryUtil.close(connection);
    }

    /**
     * 通过事务模式消息投递的可靠性
     */
    private static void invoke_push_message_transaction() throws IOException{

        Connection connection = ConnectionFactoryUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("test_transaction_queue", false, false, false, null);


        try {
            channel.txSelect();
            channel.basicPublish("", "test_transaction_queue", null, "transaction comit message".getBytes());
            channel.txCommit();
            System.out.println("消息发送成功");
        } catch (Exception ex) {
            channel.txRollback();
            System.out.println("消息发送失败");
        }
        ConnectionFactoryUtil.close(connection);

    }

    /**
     * 优先级别高的消息、优先投递
     */
    private static void invoke_push_message_prority() throws IOException{

        Connection connection = ConnectionFactoryUtil.getConnection();
        Channel channel = connection.createChannel();

        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", 10);
        // 通过队列、指定消息的过期时间
        channel.queueDeclare("test_priority_queue", false, false, false, args);


        for (int i = 0; i < 5; i++) {
            String message = "第" + i + "条消息入队";
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                    .deliveryMode(2) // 持久化
                    .contentEncoding("UTF-8")
                    .priority(i)
                    .build();
            channel.basicPublish("","test_priority_queue",properties,message.getBytes());
        }
        ConnectionFactoryUtil.close(connection);
    }

    /**
     * 发送消息变成死信、进入死信交换机
     */
    private static void invoke_push_message_dlx() throws IOException{

        Connection connection = ConnectionFactoryUtil.getConnection();
        Channel channel = connection.createChannel();

        // 通过队列、指定消息的过期时间
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-length", 3);
        args.put("x-dead-letter-exchange", "DLX_EXCHANGE");
        channel.queueDeclare("test_dlx_queue", false, false, false, args);

        channel.exchangeDeclare("DLX_EXCHANGE", "topic", false, false, null);

        channel.queueDeclare("DLX_QUEUE", false, false, false, null);

        channel.queueBind("DLX_QUEUE", "DLX_EXCHANGE", "#");


        for (int i = 0; i < 5; i++) {
            String message = "第" + i + "条消息入队";
            channel.basicPublish("","test_dlx_queue",null,message.getBytes());
        }
        ConnectionFactoryUtil.close(connection);


    }

    /**
     * 设置发送消息的过期时间
     */
    private static void invoke_push_message_setting_ttl() throws IOException{

        Connection connection = ConnectionFactoryUtil.getConnection();
        Channel channel = connection.createChannel();

        String message = " hello world ,rabbitmq ttl msg";

        // 通过队列、指定消息的过期时间
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 6000);
        channel.queueDeclare("test_ttl_queue", false, false, false, args);

        // 单独对消息设置过期时间
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .deliveryMode(2) // 持久化
                .contentEncoding("UTF-8")
                .expiration("10000") // 过期时间 单位是毫秒
                .build();

        channel.basicPublish("","test_ttl_queue",properties,message.getBytes());
        ConnectionFactoryUtil.close(connection);
    }

    /**
     * 根据已近创建好的交换机和队列绑定关系，直接将消息发送到交互及
     */
    private static void invoke_push_message_toExhange() throws IOException{

        Connection connection = ConnectionFactoryUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.basicPublish("simpleExchange","mq",null,"fighing".getBytes());

    }

    /**
     * 根据交换机灵活路由消息
     *
     * 交换机类型 direct 、fanout、topic、header
     */
    private static void invoke_push_message_toBindQueue() {
        new Thread(()->{
            try {

                Connection connection = ConnectionFactoryUtil.getConnection();
                if (connection != null) {

                    Channel channel = connection.createChannel();
                    // 交换机类型 direct 、fanout、topic
                    channel.exchangeDeclare(RabbitConfig.INFO_EXCHANGE_NAME, "direct");

                    channel.queueDeclare(RabbitConfig.INFO_QUEUE_NAME, false, false, false, null);

                    channel.queueDeclare(RabbitConfig.ERROR_QUEUE_NAME, false, false, false, null);

                    channel.queueBind(RabbitConfig.ERROR_QUEUE_NAME, RabbitConfig.INFO_EXCHANGE_NAME, "error_level");
                    channel.queueBind(RabbitConfig.INFO_QUEUE_NAME, RabbitConfig.INFO_EXCHANGE_NAME, "info_level");

                    String message = "exchange bind queue";

                    channel.basicPublish(RabbitConfig.INFO_EXCHANGE_NAME,"info",null,message.getBytes());
                    ConnectionFactoryUtil.close(connection);
                }

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        },"T3-Product").start();

    }

    /**
     * 发送消息到不绑定队列的交换机
     *
     * 消息发送到没有队列绑定的交换机时，消息将丢失，因为，交换机没有存储消息的能力，消息只能存在在队列中。
     */
    private static void invoke_push_message_toNoBindQueue() {
        new Thread(()->{
            try {

                String exchangeName = RabbitConfig.HELLO_EXCHANGE_NAME;
                Connection connection = ConnectionFactoryUtil.getConnection();
                if (connection != null) {

                    Channel channel = connection.createChannel();

                    channel.exchangeDeclare(exchangeName, "fanout");

                    String message = "exchange bind queue";

                    channel.basicPublish(exchangeName,"",null,message.getBytes());

                    ConnectionFactoryUtil.close(connection);
                }

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        },"T2-Product").start();
    }

    /**
     * 如果用空字符串去申明一个exchange，那么系统就会使用默认交换机“AMQP”,这个交换机隐式绑定了所有队列，路由key是queue的名字。
     *  绑定到这个默认的exchange上去
     * @throws IOException
     */
    private static void invoke_push_message() {

        new Thread(()->{
            try {
                String queueName = RabbitConfig.HELLO_QUEUE_NAME;
                Connection connection = ConnectionFactoryUtil.getConnection();
                if (connection != null) {

                    Channel channel = connection.createChannel();

                    channel.queueDeclare(queueName, false, false, false, null);

                    String message = "study the rabbitmq";

                    for (int i = 0; i < 5; i++) {
                        message = message + i;
                        //第一种： 直接发送消息给队列
                        channel.basicPublish("", queueName, null, message.getBytes());
                    }


                    ConnectionFactoryUtil.close(connection);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        },"T1-Product").start();

    }
}
