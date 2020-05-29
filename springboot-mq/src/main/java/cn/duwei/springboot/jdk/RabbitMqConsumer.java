package cn.duwei.springboot.jdk;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 消费者
 */
public class RabbitMqConsumer {


    public static void main(String[] args)  {
        invoke_consumer_message_autoAck();

        invoke_consumer_message();


    }


    /**
     * 手动应答,消费消息
     */
    private static void invoke_consumer_message(){

        new Thread(() -> {
            try {
                String queueName = RabbitConfig.INFO_QUEUE_NAME;
                Connection connection = ConnectionFactoryUtil.getConnection();
                if (connection != null) {

                    Channel channel = connection.createChannel();

                    channel.queueDeclare(queueName, false, false, false, null);

                    // 同一时刻服务器只会发一条消息给消费者
                    channel.basicQos(1);


                    channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                                   byte[] body) throws IOException {

                            String msg = new String(body, "UTF-8");
                            System.out.println("--------------"+Thread.currentThread().getName()+"----------------");
                            System.out.println(" Received message : '" + msg + "'");
                            System.out.println(" consumerTag : " + consumerTag);
                            System.out.println(" deliveryTag : " + envelope.getDeliveryTag());
                            // 手动应答
                            channel.basicAck(envelope.getDeliveryTag(),false);
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        },"T1-Consumer").start();

    }

    /**
     * 自动应答，消费消息
     *
     * @throws IOException
     */
    private static void invoke_consumer_message_autoAck(){

        new Thread(()->{
            try {
                String queueName = RabbitConfig.ERROR_QUEUE_NAME;
                Connection connection = ConnectionFactoryUtil.getConnection();
                if (connection != null) {

                    Channel channel = connection.createChannel();

                    channel.queueDeclare(queueName, false, false, false, null);

                    // 同一时刻服务器只会发一条消息给消费者
                    channel.basicQos(1);

                    channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                                   byte[] body) throws IOException {
                            String msg = new String(body, "UTF-8");
                            System.out.println("--------------"+Thread.currentThread().getName()+"----------------");
                            System.out.println(" Received message : '" + msg + "'");
                            System.out.println(" consumerTag : " + consumerTag);
                            System.out.println(" deliveryTag : " + envelope.getDeliveryTag());
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        },"T2-Consumer").start();

    }
}
