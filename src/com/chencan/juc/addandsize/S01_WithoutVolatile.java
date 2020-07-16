/**
 * 实现一个容器，提供两个方法add、size，写两个线程：
 * 线程1，添加10个元素到容器中
 * 线程2，实时监控元素个数，当个数到5个时，线程2给出提示并结束
 */
/**
 * 没有按预期执行的原因
 * 1.没有加同步
 * 2.while(true)中的c.size()方法永远没有检测到，原因是线程与线程之间是不可见的
 */
package com.chencan.juc.addandsize;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class S01_WithoutVolatile {

    List list = new ArrayList();

    public void add(Object o) {
        list.add(o);
    }

    public int size() {
        return list.size();
    }

    public static void main(String[] args) {
        S01_WithoutVolatile c = new S01_WithoutVolatile();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                c.add(new Object());
                System.out.println("add " + i);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t1").start();

        new Thread(()->{
            while (true) {
                if (c.size() == 5) {
                    break;
                }
            }
            System.out.println("t2 结束");
        },"t2").start();
    }
}
