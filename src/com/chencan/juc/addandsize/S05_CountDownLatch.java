/**
 * 实现一个容器，提供两个方法add、size，写两个线程：
 * 线程1，添加10个元素到容器中
 * 线程2，实时监控元素个数，当个数到5个时，线程2给出提示并结束
 */
/**
 *没有按预期执行的原因
 * 在t1线程里，对象增加到5个时，t2线程的门拴确实被打开了，但是t1线程马上又会接着执行，
 * 之前是t1会休眠1秒，给t2线程执行时间，但当注释掉休眠1秒这段代码，t2就没有机会去实时
 * 监控了
 */
/**
 * 使用Latch（门闩）替代wait notify来进行通知
 * 好处是通信方式简单，同时也可以指定等待时间
 * 使用await和countdown方法替代wait和notify
 * CountDownLatch不涉及锁定，
 * 当count的值为零时当前线程继续运行
 * 当不涉及同步，只是涉及线程通信的时候，用synchronized + wait/notify就显得太重了
 * 这时应该考虑countdownlatch/cyclicbarrier/semaphore
 */
package com.chencan.juc.addandsize;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class S05_CountDownLatch {
    //添加volatile,使t2能够得到通知
    volatile List list = new ArrayList<>();

    public void add(Object o) {
        list.add(o);
    }

    public int getSize() {
        return list.size();
    }

    public static void main(String[] args) {
        S05_CountDownLatch c = new S05_CountDownLatch();
        CountDownLatch latch = new CountDownLatch(1);
        //需要注意先启动t2再启动t1,首先让t2进入监控状态，以完成实时监控
        new Thread(()->{
            System.out.println("t2启动");
            if (c.getSize() != 5) {
                try {
                    latch.await();//自己阻塞
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("t2 结束");
        },"t2").start();


        new Thread(()->{
            System.out.println("t1 启动");
            for (int i = 0; i < 10; i++) {
                c.add(new Object());
                System.out.println("add " + i);
                if (c.getSize() == 5) {
                    latch.countDown();//唤醒t2线程
                }
//                try {
//                    TimeUnit.SECONDS.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        },"t1").start();
    }
}
