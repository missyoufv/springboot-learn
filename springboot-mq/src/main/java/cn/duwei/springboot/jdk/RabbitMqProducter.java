package cn.duwei.springboot.jdk;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

/**
 * 生产者
 */
public class RabbitMqProducter {

    public static void main(String[] args)  {

//        invoke_push_message();
        invoke_push_message_toNoBindQueue();
        invoke_push_message_toBindQueue();
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
