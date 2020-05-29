package cn.duw.springboot.zookeeper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderNumGenerator {


    //全局订单id;
    public static int count = 0;

    public String getNumber() {

        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpt = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpt.format(new Date()) + "" + ++count;
    }
}
