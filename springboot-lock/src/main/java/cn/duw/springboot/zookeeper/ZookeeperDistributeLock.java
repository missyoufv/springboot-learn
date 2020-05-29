package cn.duw.springboot.zookeeper;

import org.I0Itec.zkclient.IZkDataListener;

import java.util.concurrent.CountDownLatch;

public class ZookeeperDistributeLock extends ZookeeperAbstractLock {

    private CountDownLatch countDownLatch = null;

    @Override
    protected void waitLock() {

        // 监听器
        IZkDataListener iZkDataListener = new IZkDataListener() {

            @Override
            public void handleDataChange(String s, Object o) {

                if (countDownLatch != null) {

                    countDownLatch.countDown();
                }
            }

            @Override
            public void handleDataDeleted(String s) {

            }
        };
        zkClient.subscribeDataChanges(LOCK_ROOT, iZkDataListener);

        if (zkClient.exists(LOCK_ROOT)) {
            countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        zkClient.unsubscribeDataChanges(LOCK_ROOT,iZkDataListener);
    }

    /**
     * 尝试获取锁
     *
     * @return
     */
    @Override
    protected boolean tryLock() {
        try {
            zkClient.createEphemeral(LOCK_ROOT);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
