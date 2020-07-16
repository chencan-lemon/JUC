/**
 * 实现一个容器，提供两个方法add、size，写两个线程：
 * 线程1，添加10个元素到容器中
 * 线程2，实时监控元素个数，当个数到5个时，线程2给出提示并结束
 */
/**
 *注释掉t1线程里的TimeUnit.SECONDS.sleep(1);程序还是无法运行成功
 * 在t1线程调用unpark()方法唤醒t2线程的时候，t1线程并没有停止，
 * 造成t2线程无法及时的打印提示信息
 */
package com.chencan.juc.addandsize;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class S07_LockSupport {
    //添加volatile,使t2能够得到通知
    volatile List list = new ArrayList<>();

    public void add(Object o) {
        list.add(o);
    }

    public int getSize() {
        return list.size();
    }

    public static void main(String[] args) {
        S07_LockSupport c = new S07_LockSupport();

        //需要注意先启动t2再启动t1,首先让t2进入监控状态，以完成实时监控
        Thread t2 = new Thread(() -> {
            System.out.println("t2启动");
            if (c.getSize() != 5) {
                LockSupport.park();
            }
            System.out.println("t2 结束");


        }, "t2");

        t2.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        new Thread(() -> {
            System.out.println("t1启动");
            for (int i = 0; i < 10; i++) {
                c.add(new Object());
                System.out.println("add " + i);

                if (c.getSize() == 5) {
                    LockSupport.unpark(t2);
                }

//				try {
//					TimeUnit.SECONDS.sleep(1);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
            }

        }, "t1").start();
    }
}
