/**
 * 实现一个容器，提供两个方法add、size，写两个线程：
 * 线程1，添加10个元素到容器中
 * 线程2，实时监控元素个数，当个数到5个时，线程2给出提示并结束
 */
/**
 * 按预期执行的原因
 * notify()之后，t1必须释放锁(调用了wait()方法阻塞了t1线程)，
 * 实现了t2线程的实时监控，t2线程执行结束，打印出相应提示，
 * 最后调用notify()方法唤醒t1线程，让t1线程完成执行
 */
package com.chencan.juc.addandsize;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class S04_NotifyFreeLock {
    //添加volatile,使t2能够得到通知
    volatile List list = new ArrayList<>();

    public void add(Object o) {
        list.add(o);
    }

    public int getSize() {
        return list.size();
    }

    public static void main(String[] args) {
        S04_NotifyFreeLock C = new S04_NotifyFreeLock();
        final Object lock = new Object();
        //需要注意先启动t2再启动t1,首先让t2进入监控状态，以完成实时监控
        new Thread(()->{
            synchronized (lock) {
                System.out.println("t2 启动");
                if (C.getSize() != 5) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("t2 结束");
                //通知t1继续执行
                lock.notify();
            }
        },"t2").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(()->{
            System.out.println("t1 启动");
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {
                    C.add(new Object());
                    System.out.println("add " + i);
                    if (C.getSize() == 5) {
                        lock.notify();
                        //释放锁，让t2能执行
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"t1").start();
    }
}
