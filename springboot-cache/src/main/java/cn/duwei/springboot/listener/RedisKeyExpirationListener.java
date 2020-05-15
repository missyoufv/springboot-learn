package cn.duwei.springboot.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * 编写Redis过期回调监听方法，必须继承KeyExpirationEventMessageListener ，有点类似于MQ的消息监听。
 *
 * redis-cluster不支持key过期监听，建立多个连接，对每个redis节点进行监听
 * 单节点写法如下
 */
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    /**
     * redis过期回调
     * @param listenerContainer
     */
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        System.out.println(new String(message.getBody()));
        System.out.println("监听到key：" + expiredKey + "已过期");
    }

}
