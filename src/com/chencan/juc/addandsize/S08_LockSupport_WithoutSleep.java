/**
 * 实现一个容器，提供两个方法add、size，写两个线程：
 * 线程1，添加10个元素到容器中
 * 线程2，实时监控元素个数，当个数到5个时，线程2给出提示并结束
 */
/**
 * 按预期执行的原因
 * t1线程调用unpark()方法唤醒t2线程的时候，紧接着调用park()方法使t1线程阻塞，
 * 实现了t2线程的实时监控，t2线程执行结束，打印出相应提示，
 * 最后调用unpark()方法唤醒t1线程，让t1线程完成执行
 */
package com.chencan.juc.addandsize;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

public class S08_LockSupport_WithoutSleep {
    //添加volatile,使t2能够得到通知
    volatile List list = new ArrayList<>();

    public void add(Object o) {
        list.add(o);
    }

    public int getSize() {
        return list.size();
    }

    static Thread t1 = null, t2 = null;

    public static void main(String[] args) {
        S08_LockSupport_WithoutSleep c = new S08_LockSupport_WithoutSleep();

        t2 = new Thread(() -> {
            System.out.println("t2启动");
            if (c.getSize() != 5) {
                LockSupport.park();
            }
            System.out.println("t2 结束");
            LockSupport.unpark(t1);
        }, "t2");


        t1 = new Thread(() -> {
            System.out.println("t1启动");
            for (int i = 0; i < 10; i++) {
                c.add(new Object());
                System.out.println("add " + i);

                if (c.getSize() == 5) {
                    LockSupport.unpark(t2);
                    LockSupport.park();
                }
            }
        }, "t1");
        //需要注意先启动t2再启动t1,首先让t2进入监控状态，以完成实时监控
        t2.start();
        t1.start();
    }
}
