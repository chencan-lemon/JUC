/**
 * 实现一个容器，提供两个方法add、size，写两个线程：
 * 线程1，添加10个元素到容器中
 * 线程2，实时监控元素个数，当个数到5个时，线程2给出提示并结束
 */
/**
 *按预期执行的原因
 * 在t1线程打开t2线程门拴的时候，让t1线程再给自己添加一个门拴
 */
/**
 * 使用Latch（门闩）替代wait notify来进行通知
 * 好处是通信方式简单，同时也可以指定等待时间
 * 使用await和countdown方法替代wait和notify
 * CountDownLatch不涉及锁定，
 * 当count的值为零时,当前线程继续运行
 * 当不涉及同步，只是涉及线程通信的时候，用synchronized + wait/notify就显得太重了
 * 这时应该考虑countdownlatch/cyclicbarrier/semaphore
 */
package com.chencan.juc.addandsize;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class S06_CountDownLatch_WithoutSleep {
    //添加volatile,使t2能够得到通知
    volatile List list = new ArrayList<>();

    public void add(Object o) {
        list.add(o);
    }

    public int getSize() {
        return list.size();
    }

    public static void main(String[] args) {
        S06_CountDownLatch_WithoutSleep c = new S06_CountDownLatch_WithoutSleep();
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
                    //打开门拴，让t2线程执行，即唤醒t2线程
                    latch.countDown();
                    //给t1线程上门拴，让t2有机会执行
                    try {
                        latch.await();//自己阻塞
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"t1").start();
    }
}
