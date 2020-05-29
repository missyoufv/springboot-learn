package cn.duwei.springboot.jdk;

import com.rabbitmq.client.*;

import java.io.IOException;

public class BasicConsumerImpl implements Consumer {

    private Channel channel;

    public BasicConsumerImpl(Channel channel) {
        this.channel = channel;
    }
    @Override
    public void handleConsumeOk(String consumerTag) {
        System.out.println("isOk");
    }

    @Override
    public void handleCancelOk(String consumerTag) {

    }

    @Override
    public void handleCancel(String consumerTag) {

    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {

    }

    @Override
    public void handleRecoverOk(String consumerTag) {

    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("consumerTag is :" + consumerTag + " envelope is :" + envelope.toString()+" properties is :" + properties + "body is "+ new String(body));
        channel.basicAck(envelope.getDeliveryTag(),false);
    }
}
