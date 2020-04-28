package cn.duw.springboot.thread.study;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadStudy {

        private volatile int count = 0;
//    private AtomicInteger count = new AtomicInteger(0);
//    static CountDownLatch countDownLatch = new CountDownLatch(10);

    public static void main(String[] args) throws Exception{
        ThreadStudy threadStudy = new ThreadStudy();

        for (int i = 1; i <= 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 50; j++) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    threadStudy.count.getAndIncrement();
                    threadStudy.count = threadStudy.count +1 ;
                    System.out.println(Thread.currentThread().getName() + " : "+ threadStudy.count);
                }
//                countDownLatch.countDown();
            },"thread"+i).start();
        }
//        countDownLatch.await();
        Thread.sleep(200);
        System.out.println(threadStudy.count);
    }
}
