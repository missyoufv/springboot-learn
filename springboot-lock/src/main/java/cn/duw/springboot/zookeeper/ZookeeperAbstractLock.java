package cn.duw.springboot.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;

/**
 * 使用模板设计模式
 */
@Slf4j
public abstract class ZookeeperAbstractLock implements Lock {

    private static final String CONNECTION_URL = "127.0.0.1:2181";

    protected static final String LOCK_ROOT = "/lock";

    protected  ZkClient zkClient;


    public ZookeeperAbstractLock() {
        zkClient = new ZkClient(CONNECTION_URL);
    }

    /**
     * 获取锁
     */
    @Override
    public void getLock() {
        if (!tryLock()) {
            // 获取锁失败、阻塞
            waitLock();
            // 重新获取锁
            getLock();
        }else {
            log.info( Thread.currentThread().getName() + " get the lock");
        }
    }

    /**
     * 释放锁
     */
    @Override
    public void unLock() {

        if (zkClient != null) {
            zkClient.close();
            log.info( Thread.currentThread().getName() + " release the lock");
        }
    }


    /**
     * 等待锁
     */
    protected abstract void waitLock();


    protected abstract boolean tryLock();
}
