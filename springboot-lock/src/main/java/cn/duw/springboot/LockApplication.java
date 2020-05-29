package cn.duw.springboot;

/**
 *  分布式锁的实现：
 *      基于db（新建一个表、主见、方法名、线程id 、重入次数  根据是否能插入控制是否能获取锁 缺点：db性能影响获取锁的性能、锁不能自动释放）
 *      基于redis集群
 *      基于zookeeper
 */
public class LockApplication {


    public static void main(String[] args) {

    }
}
