package cn.duw.springboot.zookeeper;

public class OrderService implements Runnable {

    private OrderNumGenerator orderNumGenerator = new OrderNumGenerator();

    private ZookeeperDistributeLock lock = new ZookeeperDistributeLock();

    @Override
    public void run() {

        try {
            lock.getLock();
            String number = orderNumGenerator.getNumber();
            System.out.println(Thread.currentThread().getName() + ",生成订单ID:" + number);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unLock();
        }

    }


    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            new Thread( new OrderService()).start();
        }
    }
}
