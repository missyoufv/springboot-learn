package cn.duwei.springboot.jdk;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class ConnectionFactoryUtil {

    private static ConnectionFactory connectionFactory = null;

    static {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("duwei");
        connectionFactory.setPassword("duwei");
        connectionFactory.setHost("192.168.154.129");
        connectionFactory.setPort(5672);
    }

    private ConnectionFactoryUtil() {

    }

    public static Connection getConnection() {
        try {
            return connectionFactory.newConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void close(Connection connection) {
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
